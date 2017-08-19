package com.example.erick.reportedecaos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements ProfileFragment.OnFragmentInteractionListener,
                    ReportFragment.OnFragmentInteractionListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private DatabaseReference reportsReference;
    private ChildEventListener childReportEventListener;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MyViewPagerAdapter adapter;

    public static final String REPORTS_CHILD = "reports";
    public static final String USERS_CHILD = "users";
    public static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase Components
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        reportsReference = firebaseDatabase.child(REPORTS_CHILD);
        firebaseAuth = FirebaseAuth.getInstance();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new MyViewPagerAdapter(
                getSupportFragmentManager()
        );
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Set icons
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_notifications_black_24dp);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_chat_bubble_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_black_24dp);
    }

    @Override
    public void signOut() {
        firebaseAuth.signOut();
    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        private static final int TAB_COUNT = 2;

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;

            switch (position) {
                case 1:
                    fragment = ProfileFragment.newInstance();
                    break;
                case 0:
                default:
                    fragment = ReportFragment.newInstance();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (authStateListener == null) {
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    adapter = new MyViewPagerAdapter(getSupportFragmentManager());
                    int index = tabLayout.getSelectedTabPosition();
                    viewPager.setAdapter(adapter);
                    tabLayout.setupWithViewPager(viewPager);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_black_24dp);
                    tabLayout.getTabAt(index).select();
                }
            };

            firebaseAuth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
            authStateListener = null;
        }
    }
}
