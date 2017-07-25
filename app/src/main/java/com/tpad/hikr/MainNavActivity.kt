package com.tpad.hikr

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ResultCodes
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.tpad.hikr.Fragments.ActiveHikesFragment
import com.tpad.hikr.Fragments.HikrDiaryFragment
import com.tpad.hikr.Fragments.HikrDiscoverFragment
import com.tpad.hikr.Fragments.HomeFragment
import java.util.*

/**
 * Created by oguns on 7/23/2017.
 */
class MainNavActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private val TAG = MainNavActivity::class.java.simpleName
    internal lateinit var noNetwork: LinearLayout
    internal lateinit var fragmentFrame: FrameLayout

    //GOOGLE APIS

    private var googleMap: GoogleMap? = null
    private var mCameraPosition: CameraPosition? = null
    private lateinit var googleApiClient: GoogleApiClient

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private var mLocationPermissionGranted: Boolean = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var mLastKnownLocation: Location? = null

    // Used for selecting the current place.
    private val mMaxEntries = 5
    private var mLikelyPlaceNames = ArrayList<String>()
    private var mLikelyPlaceAddresses = ArrayList<String>()
    //private var mLikelyPlaceAttributions = ArrayList<String>()
    private var mLikelyPlaceLatLngs = ArrayList<LatLng>()

    internal lateinit var mainFragManager: FragmentManager
    internal var homeFragment: HomeFragment? = null
    internal lateinit var hikrDiaryFragment: HikrDiaryFragment
    internal lateinit var activeHikesFragment: ActiveHikesFragment

    internal var savedUsed = false
    internal var latDataAvailable = false

    private var mAuth: FirebaseAuth? = null
    private var headerView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_nav)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        noNetwork = findViewById(R.id.no_network_layout) as LinearLayout
        fragmentFrame = findViewById(R.id.main_frame) as FrameLayout

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        noNetwork.setOnClickListener {
            if (isConnected) {
                setUpView()
            }
        }

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable<Location>(KEY_LOCATION)
            mCameraPosition = savedInstanceState.getParcelable<CameraPosition>(KEY_CAMERA_POSITION)
            mLikelyPlaceLatLngs = savedInstanceState.getParcelableArrayList(KEY_LATLNG)
            savedUsed = true
            Log.d(TAG, "savedInstance available")
        }
        if (isConnected) {
            setUpView()
        } else {
            fragmentFrame.visibility = View.GONE
            noNetwork.visibility = View.VISIBLE
        }
        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this as GoogleApiClient.OnConnectionFailedListener /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this as GoogleApiClient.ConnectionCallbacks)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build()
        googleApiClient.connect()
    }

    fun setUpView() {
        noNetwork.visibility = View.GONE
        fragmentFrame.visibility = View.VISIBLE
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        mAuth = FirebaseAuth.getInstance()

        headerView = navigationView.getHeaderView(0)
        setupHeaderView()

        homeFragment = HomeFragment()
        hikrDiaryFragment = HikrDiaryFragment()
        activeHikesFragment = ActiveHikesFragment()

        mainFragManager = supportFragmentManager

        Log.d(TAG, "APP STARTED")
    }

    private fun setupApiClient(){

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap?.cameraPosition)
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation)
            outState.putParcelableArrayList(KEY_LATLNG, mLikelyPlaceLatLngs)
        }
    }

    override fun onPause() {
        super.onPause()
        if (mainFragManager.findFragmentByTag(HOME_TAG) != null) {
            mainFragManager.findFragmentByTag(HOME_TAG).retainInstance = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (mainFragManager.findFragmentByTag(HOME_TAG) != null) {
            mainFragManager.findFragmentByTag(HOME_TAG).retainInstance
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.nav_home) {
            checkFragmentWithTagAndDecide(HOME_TAG)
        } else if (id == R.id.nav_discover) {
            checkFragmentWithTagAndDecide(DISCOVER_TAG)
        } else if (id == R.id.nav_active_hikes) {
            checkFragmentWithTagAndDecide(ACTIVE_TAG)
        } else if (id == R.id.nav_hikr_diary) {
            checkFragmentWithTagAndDecide(DIARY_TAG)
        } else if (id == R.id.action_settings) {
            val intent = Intent(this@MainNavActivity, MapsActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_logout) {
            mAuth?.signOut().let {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(
                                        Arrays.asList<AuthUI.IdpConfig>(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                                AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                                .build(),
                        RC_SIGN_IN)

            }
            //finish()
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onConnected(bundle: Bundle?) {
        Log.d(TAG, "onConnected")
        if(mLikelyPlaceLatLngs.isEmpty()) {
            Log.d(TAG, "latlng is empty")
            showCurrentPlace()
        }
    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        showCurrentPlace()
    }

    /**
     * Handles the result of the request for location permissions.
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
        showCurrentPlace()
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private fun showCurrentPlace() {
        Log.d(TAG, "Entered showCUrrentPlace()")

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            val thread = Thread(Runnable {
                val result = Places.PlaceDetectionApi
                        .getCurrentPlace(googleApiClient, null)
                result.setResultCallback { likelyPlaces ->
                    var i = 0
                    for (placeLikelihood in likelyPlaces) {
                        // Build a list of likely places to show the user. Max 5.
                        mLikelyPlaceNames.add(placeLikelihood.place.name as String)
                        mLikelyPlaceAddresses.add(placeLikelihood.place.address as String)
                        //mLikelyPlaceAttributions.add(placeLikelihood.place.attributions as String)
                        mLikelyPlaceLatLngs.add(placeLikelihood.place.latLng)
                        i++
                        if (i > mMaxEntries - 1) {
                            break
                        }
                    }
                    // Release the place likelihood buffer, to avoid memory leaks.
                    likelyPlaces.release()

                    checkFragmentWithTagAndDecide(HOME_TAG)

                }
            })

            thread.start()
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult called")
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                //Intent intent = new Intent(SplashActivity.this, MainNavActivity.class);
                //startActivity(intent);
                Toast.makeText(this, "Sign in succesful", Toast.LENGTH_LONG).show()
                return
            } else {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun checkFragmentWithTagAndDecide(tag: String){
        val fragment = mainFragManager.findFragmentByTag(tag)
        val fragList = mainFragManager.fragments
        if(fragment == null && fragList.isEmpty()){
            addFragment(tag)
        }
        else {
            replaceFragment(tag)
        }
    }

    private fun getFragment():Fragment?{
        var fragment: Fragment? = null
        for (i in mainFragManager.fragments){
            if(i.isVisible){
                return i
            }
        }
        return null
    }

    private fun addFragment(tag: String){
        if(tag == HOME_TAG)
            mainFragManager.beginTransaction().add(R.id.main_frame, HomeFragment(mLikelyPlaceLatLngs[0], googleApiClient), tag).addToBackStack(null).commit()
        else if(tag == DISCOVER_TAG)
            mainFragManager.beginTransaction().add(R.id.main_frame, HikrDiscoverFragment(), tag).addToBackStack(tag).addToBackStack(null).commit()
        else if(tag == DIARY_TAG)
            mainFragManager.beginTransaction().add(R.id.main_frame, HikrDiaryFragment(), tag).addToBackStack(tag).addToBackStack(null).commit()
        else if(tag == ACTIVE_TAG)
            mainFragManager.beginTransaction().add(R.id.main_frame, ActiveHikesFragment(), tag).addToBackStack(tag).addToBackStack(null).commit()
        Log.d(TAG, "Fragment added")

    }

    private fun replaceFragment(tag: String){
        //val count = mainFragManager.backStackEntryCount
        //val oldTag = mainFragManager.getBackStackEntryAt(count-1).name
        val oldFrag = getFragment()
        val fragment = mainFragManager.findFragmentByTag(tag)
        if(fragment == null) {
            if (tag == HOME_TAG)
                mainFragManager.beginTransaction().replace(R.id.main_frame, HomeFragment(mLikelyPlaceLatLngs[0], googleApiClient), tag).addToBackStack(null).commit()
            else if (tag == DISCOVER_TAG)
                mainFragManager.beginTransaction().replace(R.id.main_frame, HikrDiscoverFragment(), tag).addToBackStack(null).commit()
            else if (tag == DIARY_TAG)
                mainFragManager.beginTransaction().replace(R.id.main_frame, HikrDiaryFragment(), tag).addToBackStack(null).commit()
            else if (tag == ACTIVE_TAG)
                mainFragManager.beginTransaction().replace(R.id.main_frame, ActiveHikesFragment(), tag).addToBackStack(null).commit()
            Log.d(TAG, "new fragment used")
        }
        else{
            mainFragManager.beginTransaction().replace(R.id.main_frame, fragment, tag).addToBackStack(null).commit()
            Log.d(TAG, "old fragment used")
        }
        Log.d(TAG, "Fragment replaced")
    }
    val isConnected: Boolean
        get() {
            val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

            return isConnected
        }

    fun setupHeaderView() {
        val user = mAuth?.currentUser

        if (user != null) {
            val profilePic = headerView?.findViewById(R.id.nav_profile_pic) as ImageView
            val userName = headerView?.findViewById(R.id.nav_username) as TextView
            Glide.with(this).load(user.photoUrl).into(profilePic)
            userName.text = user.displayName
        }
    }


    companion object {
        private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        private val KEY_CAMERA_POSITION = "camera_position"
        private val KEY_LOCATION = "location"
        private val KEY_LATLNG = "latlng"
        private val HOME_TAG = "home"
        private val DISCOVER_TAG = "discover"
        private val DIARY_TAG = "diary"
        private val ACTIVE_TAG = "active"
        private val RC_SIGN_IN = 9949
    }


}