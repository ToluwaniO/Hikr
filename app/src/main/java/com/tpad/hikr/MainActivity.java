package com.tpad.hikr;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tpad.hikr.Fragments.ActiveHikesFragment;
import com.tpad.hikr.Fragments.HikerDiaryFragment;
import com.tpad.hikr.Fragments.HomeFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    FirebaseUser currentUser;

    private FirebaseAuth mAuth;

    private static final int RC_SIGN_IN = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            openAuth();
        }
        else {

        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            if(resultCode == ResultCodes.OK){
                Toast.makeText(this, "Signed in", Toast.LENGTH_LONG).show();
            }
            else {
                finish();
                Toast.makeText(this, "Signed in failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        HomeFragment homeFragment = new HomeFragment();

        Bundle bundle = new Bundle();
        bundle.putString("name", currentUser.getDisplayName());
        bundle.putString("imageUrl", currentUser.getPhotoUrl().toString());
        homeFragment.setArguments(bundle);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(homeFragment, "Home");
        adapter.addFragment(new ActiveHikesFragment(), "Active Hikes");
        adapter.addFragment(new HikerDiaryFragment(), "Hikr Diary");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> titleArrayList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title){
            fragmentArrayList.add(fragment);
            titleArrayList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("FRAGMENT ITEM", "ffffffffff");
            return fragmentArrayList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }
    }

    public void signOut(){
        if(mAuth.getCurrentUser() != null){
            mAuth.signOut();
            openAuth();
            Toast.makeText(this, "Signed out early!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "You're not signed in", Toast.LENGTH_LONG).show();
        }
    }

    public void openAuth() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.authTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);
    }
}
