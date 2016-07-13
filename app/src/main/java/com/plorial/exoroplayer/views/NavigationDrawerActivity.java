package com.plorial.exoroplayer.views;

import android.Manifest;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.model.Item;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FileChooseFragment.OnFileSelectedListener {

    private static final String TAG = NavigationDrawerActivity.class.getSimpleName();
    public Bundle fileExplorerBundle;
    public FirebaseAnalytics firebaseAnalytics;
    private AdView adView;
    private static InterstitialAd ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle analytics = new Bundle();
        analytics.putString(FirebaseAnalytics.Param.ITEM_NAME,"Exoro Started");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, analytics);

        if (savedInstanceState == null) {
            SeriesFragment fragment = new SeriesFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermissions();
            }
        }else {
            fileExplorerBundle = savedInstanceState.getBundle(this.getClass().getSimpleName());
        }
        setUpBottomAd();
        setUpAd();
    }

    private void setUpBottomAd() {
        MobileAds.initialize(this, getString(R.string.ads_app_id));
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void setUpAd() {
        ad = new InterstitialAd(this);
        ad.setAdUnitId(getString(R.string.pause_banner_id));
    }

    private void checkPermissions() {
        MultiplePermissionsListener dialogMultiplePermissionsListener =
                DialogOnAnyDeniedMultiplePermissionsListener.Builder
                        .withContext(getApplicationContext())
                        .withTitle(R.string.rt_permission_title)
                        .withMessage(R.string.rt_permission_message)
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.mipmap.ic_launcher)
                        .build();
        Dexter.checkPermissions(dialogMultiplePermissionsListener, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_EXTERNAL_STORAGE);
        Dexter.continuePendingRequestsIfPossible(dialogMultiplePermissionsListener);
    }

    @Override
    public void onFileSelected(Item o) {

        Bundle bundle = new Bundle();
        bundle.putString(FileExplorerFragment.FILE_PATH,o.getPath());
        bundle.putBundle(FileExplorerFragment.CLASS_NAME, fileExplorerBundle);
        FileExplorerFragment fragment = new FileExplorerFragment();

        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(this.getClass().getSimpleName(), fileExplorerBundle);
    }

    @Subscribe
    public void onBundleEvent(Bundle bundle){
        fileExplorerBundle = bundle;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(adView != null)
        adView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adView != null)
        adView.resume();
        loadAd();
    }

    public void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        ad.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adView != null)
        adView.destroy();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_open) {
            FileExplorerFragment fileExplorerFragment = new FileExplorerFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fileExplorerFragment);
            transaction.commit();
        } else if (id == R.id.nav_series) {
            SeriesFragment fragment = new SeriesFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        } else if(id == R.id.nav_ad){
            if(ad.isLoaded()){
                ad.show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SeriesFragment fragment = new SeriesFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
