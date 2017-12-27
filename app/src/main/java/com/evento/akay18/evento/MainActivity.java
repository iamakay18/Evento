package com.evento.akay18.evento;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AHBottomNavigation bottomNavigation;
    private FirebaseAuth mAuth;
    private NoSwipePager viewPager;
    private BottomBarAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        Button button = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        //Bottom Navigation Bar
        bottomNavigation = findViewById(R.id.bottomNavigation);
        AHBottomNavigationItem item_feed = new AHBottomNavigationItem(R.string.item_feed, R.drawable.ic_home_black_24dp, R.color.item_feed);
        AHBottomNavigationItem item_profile = new AHBottomNavigationItem(R.string.item_profile, R.drawable.ic_person_black_24dp, R.color.item_profile);
        AHBottomNavigationItem item_set = new AHBottomNavigationItem(R.string.item_set, R.drawable.ic_settings_black_24dp, R.color.item_set);

        bottomNavigation.addItem(item_feed);
        bottomNavigation.addItem(item_profile);
        bottomNavigation.addItem(item_set);
        bottomNavigation.setCurrentItem(0);

        setBNBConditions();

        viewPager = findViewById(R.id.viewPager);
        viewPager.setPagingEnabled(false);

        Fragment feedFragment = new FeedFragment();
        Fragment profileFragment = new ProfileFragment();
        Fragment settingFragment = new SettingFragment();

        pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());
        //TODO: Add fragments to pagerAdapter P.S : DONE
        pagerAdapter.addFragments(feedFragment);
        pagerAdapter.addFragments(profileFragment);
        pagerAdapter.addFragments(settingFragment);
        viewPager.setAdapter(pagerAdapter);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(!wasSelected){
                    viewPager.setCurrentItem(position);
                }
                return true;
            }
        });
    }

    public void signOut() {
        mAuth.signOut();
    }

    private void setBNBConditions() {
        this.bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
        // Disable the translation inside the CoordinatorLayout
        this.bottomNavigation.setBehaviorTranslationEnabled(true);
        // Enable the translation of the FloatingActionButton
        //this.bottomNavigation.manageFloatingActionButtonBehavior(MainFragment.getFab());
        // Change colors
        this.bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        this.bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        // Force to tint the drawable (useful for font with icon for example)
        this.bottomNavigation.setForceTint(true);
        // Use colored navigation with circle reveal effect
        this.bottomNavigation.setColored(true);
        // Set current item programmatically
        this.bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
        // Force to tint the drawable (useful for font with icon for example)
        this.bottomNavigation.setForceTint(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    //Handle Fragments at BNB
    public class BottomBarAdapter extends SmartFragmentStatePagerAdapter{

        private final List<Fragment> fragments = new ArrayList<>();

        public BottomBarAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragments(Fragment fragment){
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}