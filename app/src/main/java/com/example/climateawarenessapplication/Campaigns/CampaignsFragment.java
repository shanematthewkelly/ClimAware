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

import com.example.climateawarenessapplication.Campaigns.CampsDetailFragment1;
import com.example.climateawarenessapplication.Campaigns.CampsDetailFragment2;
import com.example.climateawarenessapplication.Campaigns.CampsDetailFragment3;
import com.example.climateawarenessapplication.Campaigns.CampsDetailFragment4;
import com.example.climateawarenessapplication.R;


public class CampaignsFragment extends Fragment implements View.OnClickListener{

    private Button camps1, camps2, camps3, camps4;
    private CardView cardview10, cardview20, cardview30, cardview40;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_campaigns, container, false);

        //init view
        camps1 = v.findViewById(R.id.camps1);
        camps1.setOnClickListener(this);

        camps2 = v.findViewById(R.id.camps2);
        camps2.setOnClickListener(this);

        camps3 = v.findViewById(R.id.camps3);
        camps3.setOnClickListener(this);

        camps4 = v.findViewById(R.id.camps4);
        camps4.setOnClickListener(this);

        cardview10 = v.findViewById(R.id.cardview10);
        cardview20 = v.findViewById(R.id.cardview20);
        cardview30 = v.findViewById(R.id.cardview30);
        cardview40 = v.findViewById(R.id.cardview40);

        Animation TopToBottom = AnimationUtils.loadAnimation(getActivity(),R.anim.top_to_bottom);
        Animation LeftToRight = AnimationUtils.loadAnimation(getActivity(),R.anim.left_to_right);
        Animation BottomToTop = AnimationUtils.loadAnimation(getActivity(),R.anim.bottom_to_top);

        cardview10.setAnimation(TopToBottom);
        cardview20.setAnimation(LeftToRight);
        cardview30.setAnimation(LeftToRight);
        cardview40.setAnimation(BottomToTop);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camps1:
                CampsDetailFragment1 campsDetailFragment1 = new CampsDetailFragment1();
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1  = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.fragment_container, campsDetailFragment1);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
                break;

            case R.id.camps2:
                CampsDetailFragment2 campsDetailFragment2 = new CampsDetailFragment2();
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2  = fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.fragment_container, campsDetailFragment2);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
                break;

            case R.id.camps3:
                CampsDetailFragment3 campsDetailFragment3 = new CampsDetailFragment3();
                FragmentManager fragmentManager3 = getFragmentManager();
                FragmentTransaction fragmentTransaction3  = fragmentManager3.beginTransaction();
                fragmentTransaction3.replace(R.id.fragment_container, campsDetailFragment3);
                fragmentTransaction3.addToBackStack(null);
                fragmentTransaction3.commit();
                break;

            case R.id.camps4:
                CampsDetailFragment4 campsDetailFragment4 = new CampsDetailFragment4();
                FragmentManager fragmentManager4 = getFragmentManager();
                FragmentTransaction fragmentTransaction4  = fragmentManager4.beginTransaction();
                fragmentTransaction4.replace(R.id.fragment_container, campsDetailFragment4);
                fragmentTransaction4.addToBackStack(null);
                fragmentTransaction4.commit();
                break;
        }

    }

}
