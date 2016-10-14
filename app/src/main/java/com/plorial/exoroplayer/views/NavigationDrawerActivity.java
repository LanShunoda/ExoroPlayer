package com.plorial.exoroplayer.views;

import android.app.FragmentTransaction;
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
            Bundle args = new Bundle();
            args.putString(SeriesFragment.DB_SOURCE, SeriesFragment.EX);
            fragment.setArguments(args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        }else {
            fileExplorerBundle = savedInstanceState.getBundle(this.getClass().getSimpleName());
        }
        setUpBottomAd();
        setUpAd();
        loadAd();
        Log.d(TAG, "navdrawer activity on create");
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
        ad.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                ad.show();
            }
        });
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
        switch (item.getItemId()) {
            case R.id.nav_open: {
                FileExplorerFragment fileExplorerFragment = new FileExplorerFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fileExplorerFragment);
                transaction.commit();
                break;
            }
            case  R.id.nav_series_ex: {
                SeriesFragment fragment = new SeriesFragment();
                Bundle args = new Bundle();
                args.putString(SeriesFragment.DB_SOURCE, SeriesFragment.EX);
                fragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
                break;
            }
            case  R.id.nav_series_fs: {
                SeriesFragment fragment = new SeriesFragment();
                Bundle args = new Bundle();
                args.putString(SeriesFragment.DB_SOURCE, SeriesFragment.FS);
                fragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
                break;
            }
            case  R.id.nav_ad:
                loadAd();
                break;
            case R.id.nav_about: {
                AboutFragment fragment = new AboutFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
                break;
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
        Bundle args = new Bundle();
        args.putString(SeriesFragment.DB_SOURCE, SeriesFragment.EX);
        fragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
