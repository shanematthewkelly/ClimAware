package com.example.climateawarenessapplication.HomeDashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.climateawarenessapplication.Blog.BlogFragment;
import com.example.climateawarenessapplication.Blog.BotProtectionFrag;
import com.example.climateawarenessapplication.Campaigns.CampaignsFragment;
import com.example.climateawarenessapplication.News.NewsFragment;
import com.example.climateawarenessapplication.Profile.ProfileFragment;
import com.example.climateawarenessapplication.R;
import com.example.climateawarenessapplication.Settings.SettingsFragment;
import com.example.climateawarenessapplication.Statistics.StatsFragment;
import com.example.climateawarenessapplication.databinding.FragmentHomeNewBinding;

public class HomeFragment extends Fragment {

    ConstraintLayout customConstraintHome, cardOneClickable, cardTwoClickable, cardThreeClickable, cardFourClickable,cardFiveClickable, cardSixClickable;


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentHomeNewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_new, container, false);

        View v = binding.getRoot();

        Animation BottomToTop = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_to_top) ;

        customConstraintHome = v.findViewById(R.id.customConstraintHome);
        customConstraintHome.setAnimation(BottomToTop);

        cardOneClickable = v.findViewById(R.id.cardOneClickable);
        cardOneClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewsFragment newsFragment = new NewsFragment();
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1  = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.fragment_container, newsFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();

            }
        });

        cardTwoClickable = v.findViewById(R.id.cardTwoClickable);
        cardTwoClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CampaignsFragment campaignsFragment = new CampaignsFragment();
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1  = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.fragment_container, campaignsFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();

            }
        });

        cardThreeClickable = v.findViewById(R.id.cardThreeClickale);
        cardThreeClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StatsFragment statsFragment = new StatsFragment();
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1  = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.fragment_container, statsFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();

            }
        });

        cardFourClickable = v.findViewById(R.id.cardFourClickable);
        cardFourClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BotProtectionFrag botProtectionFrag = new BotProtectionFrag();
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1  = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.fragment_container, botProtectionFrag);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();

            }
        });

        cardFiveClickable = v.findViewById(R.id.cardFiveClickable);
        cardFiveClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileFragment profileFragment = new ProfileFragment();
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1  = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.fragment_container, profileFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();

            }
        });

        cardSixClickable = v.findViewById(R.id.cardSixClickable);
        cardSixClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SettingsFragment settingsFragment = new SettingsFragment();
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1  = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.fragment_container, settingsFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();

            }
        });


        return v;
    }


}
