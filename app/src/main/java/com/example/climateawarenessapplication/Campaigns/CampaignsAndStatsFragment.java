package com.example.climateawarenessapplication.Campaigns;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.climateawarenessapplication.R;
import com.example.climateawarenessapplication.Statistics.StatsFragment;


public class CampaignsAndStatsFragment extends Fragment implements View.OnClickListener{

    private Button statsButton, campsButton;
    private CardView cardview1, cardview2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_campaigns_and_stats, container, false);

        statsButton = v.findViewById(R.id.statsButton);
        statsButton.setOnClickListener(this);

        campsButton = v.findViewById(R.id.campsButton);
        campsButton.setOnClickListener(this);

        cardview1 = v.findViewById(R.id.cardview1);
        cardview1.setOnClickListener(this);

        cardview2 = v.findViewById(R.id.cardview2);
        cardview2.setOnClickListener(this);


        Animation SmallToBig = AnimationUtils.loadAnimation(getActivity(),R.anim.smalltobig);

        cardview1.setAnimation(SmallToBig);
        cardview2.setAnimation(SmallToBig);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.statsButton:
                StatsFragment statsFragment = new StatsFragment();
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1  = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.fragment_container, statsFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
                break;

            case R.id.cardview1:
                StatsFragment statsFragment3 = new StatsFragment();
                FragmentManager fragmentManager3 = getFragmentManager();
                FragmentTransaction fragmentTransaction3  = fragmentManager3.beginTransaction();
                fragmentTransaction3.replace(R.id.fragment_container, statsFragment3);
                fragmentTransaction3.addToBackStack(null);
                fragmentTransaction3.commit();
                break;

            case R.id.campsButton:
                CampaignsFragment campaignsFragment = new CampaignsFragment();
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2  = fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.fragment_container, campaignsFragment);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
                break;

            case R.id.cardview2:
                CampaignsFragment campaignsFragment1 = new CampaignsFragment();
                FragmentManager fragmentManager4 = getFragmentManager();
                FragmentTransaction fragmentTransaction4  = fragmentManager4.beginTransaction();
                fragmentTransaction4.replace(R.id.fragment_container, campaignsFragment1);
                fragmentTransaction4.addToBackStack(null);
                fragmentTransaction4.commit();
                break;

        }

    }
}
