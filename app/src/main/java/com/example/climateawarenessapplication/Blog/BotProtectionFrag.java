package com.example.climateawarenessapplication.Blog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.climateawarenessapplication.FaceDetection.FaceDetectActivity;
import com.example.climateawarenessapplication.R;

import static android.content.Context.MODE_PRIVATE;

public class BotProtectionFrag extends Fragment {

    Button verifyBotBtn;
    private LinearLayout payment_group;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //make the activity on full screen
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        View v = inflater.inflate(R.layout.fragment_bot_protection, container, false);

        Animation SmallToBig = AnimationUtils.loadAnimation(getActivity(), R.anim.smalltobig);



        payment_group = v.findViewById(R.id.payment_group);
        payment_group.setAnimation(SmallToBig);

        verifyBotBtn = v.findViewById(R.id.verifyBotBtn);
        verifyBotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), FaceDetectActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return v;

    }
    private boolean restoreUserData() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPreference", MODE_PRIVATE);
        Boolean ActivityOpenedBefore = sharedPreferences.getBoolean("alreadyCompleted", false);

        return ActivityOpenedBefore;
    }

    //method that checks if the user has already clicked the button previously
    private void saveUserData() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPreference", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putBoolean("alreadyCompleted", true);
        mEditor.commit();

    }

}
