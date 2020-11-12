package com.example.climateawarenessapplication.Introduction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.climateawarenessapplication.Core.MainActivity;
import com.example.climateawarenessapplication.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button nextButton, getStartedButton;
    int position = 0;
    Animation btnAmination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        //for when this activity is about to be launched we do a check if it has already been opened before or not
        if (restoreUserData()) {

            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //animation setup
        Animation TopToBottom = AnimationUtils.loadAnimation(this,R.anim.top_to_bottom);


        //hide action bar
        getSupportActionBar().hide();

        //init views
        tabIndicator = findViewById(R.id.tab_indicator);
        nextButton = findViewById(R.id.btn_next_intro);
        getStartedButton = findViewById(R.id.btn_get_started);
        btnAmination = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_to_bottom);


        //fill list screen
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("News Articles", "Discover our most recent and relevant news articles on ClimAware! Everything you need to know about the climate all in one place.", R.drawable.no_result));
        mList.add(new ScreenItem("Worldwide Statistics", "Find out more about what worldwide countries are affected by climate change and all their individual statistics here.", R.drawable.statsshow));
        mList.add(new ScreenItem("Our Campaigns", "Read more about our selected campaigns today! Every donation you make is one step closer to a brighter future for everybody.", R.drawable.illustration));
        mList.add(new ScreenItem("Community Blog", "Something on your mind? Let the ClimAware community know through our blogging system, we value everybody's opinion here.", R.drawable.blog));



        //setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        //setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);

        //next button on click setup
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()) {

                    position++;
                    screenPager.setCurrentItem(position);
                }

                //when we reach the last screen
                if (position == mList.size()-1) {

                    onFinalScreen();

                }
            }
        });

        //TabLayout state change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1) {

                    onFinalScreen();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Get started button click
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open MainActivity
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);

                //calling the method that checks if the user has already clicked the button previously
                saveUserData();
                finish();
            }
        });


    }

    private boolean restoreUserData() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myPreference", MODE_PRIVATE);
        Boolean ActivityOpenedBefore = sharedPreferences.getBoolean("alreadyCompleted", false);

        return ActivityOpenedBefore;
    }

    //method that checks if the user has already clicked the button previously
    private void saveUserData() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myPreference", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putBoolean("alreadyCompleted", true);
        mEditor.commit();

    }

    //function that handles showing the get started button and hides the indicators
    private void onFinalScreen() {

    nextButton.setVisibility(View.INVISIBLE);
    getStartedButton.setVisibility(View.VISIBLE);
    tabIndicator.setVisibility(View.INVISIBLE);

    //setup animation
        getStartedButton.setAnimation(btnAmination);




    }
}
