package com.tpad.hikr;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tpad.hikr.DataClasses.HikeLocationData;
import com.tpad.hikr.DataClasses.User;
import com.tpad.hikr.Fragments.ActiveHikesFragment;
import com.tpad.hikr.Fragments.HikerDiaryFragment;
import com.tpad.hikr.Fragments.HomeFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private final String TAG = MainNavActivity.class.getSimpleName();
    LinearLayout noNetwork;
    FrameLayout fragmentFrame;

    //GOOGLE APIS

    private final int maxEntries = 5;
    private GoogleMap googleMap;
    private CameraPosition mCameraPosition;
    GoogleApiClient googleApiClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_LATLNG = "latlng";
    private static final String HOME_TAG = "home";

    // Used for selecting the current place.
    private final int mMaxEntries = 5;
    private String[] mLikelyPlaceNames = new String[mMaxEntries];
    private String[] mLikelyPlaceAddresses = new String[mMaxEntries];
    private String[] mLikelyPlaceAttributions = new String[mMaxEntries];
    private LatLng[] mLikelyPlaceLatLngs = new LatLng[mMaxEntries];

    FragmentManager mainFragManager;
    HomeFragment homeFragment;
    HikerDiaryFragment hikerDiaryFragment;
    ActiveHikesFragment activeHikesFragment;
    String location;

    boolean savedUsed = false;
    boolean latDataAvailable = false;

    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;
    private static final String EXTRA_SIGNED_IN_CONFIG = "extra_signed_in_config";
    private View headerView;
    private List<HikeLocationData> locationDataList;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noNetwork = (LinearLayout)findViewById(R.id.no_network_layout);
        fragmentFrame = (FrameLayout)findViewById(R.id.main_frame);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        noNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                    setUpView(savedInstanceState == null);
                }
            }
        });

        if(savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            mLikelyPlaceLatLngs = (LatLng[]) savedInstanceState.getParcelableArray(KEY_LATLNG);
            savedUsed = true;
            Log.d(TAG, "savedInstance available");
        }
        if(isConnected()) {
            setUpView(savedInstanceState == null);
        }
        else {
            fragmentFrame.setVisibility(View.GONE);
            noNetwork.setVisibility(View.VISIBLE);
        }
        
    }

    public void setUpView(boolean dataNeeded){
        noNetwork.setVisibility(View.GONE);
        fragmentFrame.setVisibility(View.VISIBLE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();

        headerView = navigationView.getHeaderView(0);
        setupHeaderView();

        homeFragment = new HomeFragment();
        hikerDiaryFragment = new HikerDiaryFragment();
        activeHikesFragment = new ActiveHikesFragment();

        mainFragManager = getSupportFragmentManager();
        //mainFragManager.beginTransaction().add(R.id.main_frame, homeFragment).commit();

        if(dataNeeded) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */,
                            (GoogleApiClient.OnConnectionFailedListener) this /* OnConnectionFailedListener */)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
            googleApiClient.connect();
            Log.d(TAG, "lat data unavailable");
        }

        Log.d(TAG, "APP STARTED");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            outState.putParcelableArray(KEY_LATLNG, mLikelyPlaceLatLngs);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mainFragManager.findFragmentByTag(HOME_TAG) != null){
            mainFragManager.findFragmentByTag(HOME_TAG).setRetainInstance(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mainFragManager.findFragmentByTag(HOME_TAG) != null) {
            mainFragManager.findFragmentByTag(HOME_TAG).getRetainInstance();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Log.d(TAG, "HOME TAB PRESSED");
            Fragment home = (HomeFragment)mainFragManager.findFragmentByTag(HOME_TAG);
            if(home == null){
                home = new HomeFragment(mLikelyPlaceLatLngs[0], googleApiClient);
                Log.d(TAG, "Fragment is not in back stack");
            }
            else{
                Log.d(TAG, "Fragment is in back stack");
            }
            mainFragManager.beginTransaction().replace(R.id.main_frame, home).addToBackStack(null).commit();

        } else if (id == R.id.nav_discover) {
            mainFragManager.beginTransaction().replace(R.id.main_frame, hikerDiaryFragment).commit();
        } else if (id == R.id.nav_active_hikes) {
            mainFragManager.beginTransaction().replace(R.id.main_frame, activeHikesFragment).commit();
        } else if (id == R.id.nav_hikr_diary) {
            mainFragManager.beginTransaction().replace(R.id.main_frame, hikerDiaryFragment).commit();
        }
        else if(id == R.id.action_settings){
            Intent intent = new Intent(MainNavActivity.this, MapsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_logout){
            mAuth.signOut();

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(
                                    Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        showCurrentPlace();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        showCurrentPlace();
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        showCurrentPlace();
    }

    public void getLocationData(String city, String country){
        String query = city + "%20" + country + "%20hiking%20area";
        //new GetLocationData(this, googleApiClient).execute("https://us-central1-hikr-41391.cloudfunctions.net/app/locations/" + query);
    }
    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        Log.d(TAG, "Entered showCUrrentPlace()");

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    @SuppressWarnings("MissingPermission")
                    PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                            .getCurrentPlace(googleApiClient, null);
                    result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                        @Override
                        public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                            int i = 0;
                            mLikelyPlaceNames = new String[mMaxEntries];
                            mLikelyPlaceAddresses = new String[mMaxEntries];
                            mLikelyPlaceAttributions = new String[mMaxEntries];
                            mLikelyPlaceLatLngs = new LatLng[mMaxEntries];
                            for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                // Build a list of likely places to show the user. Max 5.
                                mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace().getAddress();
                                mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                        .getAttributions();
                                mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
                                i++;
                                if (i > (mMaxEntries - 1)) {
                                    break;
                                }
                            }
                            // Release the place likelihood buffer, to avoid memory leaks.
                            likelyPlaces.release();
                            LatLng a = mLikelyPlaceLatLngs[0];
                            homeFragment = new HomeFragment(a, googleApiClient);
                            mainFragManager.beginTransaction().add(R.id.main_frame, homeFragment, HOME_TAG).addToBackStack(null).commit();
                            latDataAvailable = true;
                        }
                    });
                }
            });

            thread.start();
        }

        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                //Intent intent = new Intent(SplashActivity.this, MainNavActivity.class);
                //startActivity(intent);
                Toast.makeText(this, "Sign in succesful", Toast.LENGTH_LONG).show();
                return;
            } else {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_LONG).show();
            }

        }
    }

    public boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public void setupHeaderView(){
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            ImageView profilePic = (ImageView) headerView.findViewById(R.id.nav_profile_pic);
            TextView userName = (TextView) headerView.findViewById(R.id.nav_username);
            Glide.with(this).load(user.getPhotoUrl()).into(profilePic);
            userName.setText(user.getDisplayName());
        }
    }



}