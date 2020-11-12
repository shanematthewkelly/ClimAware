package com.example.climateawarenessapplication.Campaigns;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.climateawarenessapplication.Biometrics.Biometrics;
import com.example.climateawarenessapplication.R;
import com.mikhaellopez.circularimageview.CircularImageView;

public class CampsDetailFragment2 extends Fragment  implements View.OnClickListener {

    private Button donationBtn;
    CircularImageView rainforestImage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camps2_detail, container, false);

        Animation SmallToBig = AnimationUtils.loadAnimation(getActivity(),R.anim.smalltobig);

        //init views
        rainforestImage = v.findViewById(R.id.circleview2);
        rainforestImage.setAnimation(SmallToBig);

        donationBtn = v.findViewById(R.id.donationBtn);
        donationBtn.setOnClickListener(this);



        return v;

    }

    @Override
    public void onClick(View v) {
        checkAuth(v);
    }

    private void checkAuth(View v) {

        Intent intent = new Intent(getActivity(), Biometrics.class);
        startActivity(intent);
        getActivity().finish();

    }
}
