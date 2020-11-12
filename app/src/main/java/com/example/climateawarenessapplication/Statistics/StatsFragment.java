package com.example.climateawarenessapplication.Statistics;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import com.example.climateawarenessapplication.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class StatsFragment extends Fragment {

    CardView cardViewMain, cardViewLeft, cardViewRight1, cardViewRight2, cardTop;
    ProgressBar EuropeBar, AsiaBar, AmericaBar, AfrricaBar;
    private TextView EUROPE, ASIA, AMERICAS, AFRICA, rightcardstat, rightcardstat2;
    private DatabaseReference mReference;
    int counter = 0;
    PieChart pieChart;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            View v = inflater.inflate(R.layout.prog_test, container, false);


        Animation Grow = AnimationUtils.loadAnimation(getActivity(), R.anim.smalltobig);
        Animation TopToBottom = AnimationUtils.loadAnimation(getActivity(), R.anim.top_to_bottom);
        Animation LeftToRIght = AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right);


        cardViewMain = v.findViewById(R.id.cardViewMain);
        cardViewMain.setAnimation(Grow);

        cardViewLeft = v.findViewById(R.id.cardViewLeft);
        cardViewLeft.setAnimation(TopToBottom);

        cardViewRight1 = v.findViewById(R.id.cardViewRight1);
        cardViewRight1.setAnimation(LeftToRIght);

        cardViewRight2 = v.findViewById(R.id.cardViewRight2);
        cardViewRight2.setAnimation(LeftToRIght);

        cardTop = v.findViewById(R.id.cardTop);
        cardTop.setAnimation(TopToBottom);


        //----------------- PIE CHART CODE ----------------------------------------------------//

        pieChart = v.findViewById(R.id.mypiechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.99f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        yValues.add(new PieEntry(14f, "EU"));
        yValues.add(new PieEntry(23f, "Asia"));
        yValues.add(new PieEntry(34f, "Americas"));
        yValues.add(new PieEntry(35f, "Africa"));


        PieDataSet dataSet = new PieDataSet(yValues, "Pollution Levels");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(0f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);

        //-------------------------------------------------------------------------------------//


        //init progress bars
        EuropeBar = v.findViewById(R.id.EuropeBar);
        AsiaBar = v.findViewById(R.id.AsiaBar);
        AmericaBar = v.findViewById(R.id.AmericaBar);
        AfrricaBar = v.findViewById(R.id.AfricaBar);




        myProgress();

        //init stat textviews
        EUROPE = v.findViewById(R.id.statsEurope);
        ASIA = v.findViewById(R.id.statsAsia);
        AMERICAS = v.findViewById(R.id.statsAmericas);
        AFRICA = v.findViewById(R.id.statsAfrica);
        rightcardstat = v.findViewById(R.id.rightcardstat);
        rightcardstat2 = v.findViewById(R.id.rightcardstat2);


        //retrieve a single row from Firebase database
        mReference = FirebaseDatabase.getInstance().getReference().child("stats").child("worldStats");
        mReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {

                String Europe = dataSnapshot.child("Europe").getValue().toString();
                String Asia = dataSnapshot.child("Asia").getValue().toString();
                String Americas = dataSnapshot.child("Americas").getValue().toString();
                String Africa = dataSnapshot.child("Africa").getValue().toString();
                String RightCardStat = dataSnapshot.child("RightCardStat").getValue().toString();
                String RightCardStat2 = dataSnapshot.child("RightCardStat2").getValue().toString();
                EUROPE.setText(Europe);
                ASIA.setText(Asia);
                AMERICAS.setText(Americas);
                AFRICA.setText(Africa);
                rightcardstat.setText(RightCardStat);
                rightcardstat2.setText(RightCardStat2);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "An Error Has Occurred", Toast.LENGTH_SHORT).show();
            }
        });




        return v;
        }

    private void myProgress() {

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;
                if (counter < 84 )
                    EuropeBar.setProgress(counter);

                if (counter < 68 )
                    AsiaBar.setProgress(counter);

                if (counter < 45 )
                    AmericaBar.setProgress(counter);

                if (counter < 26 )
                    AfrricaBar.setProgress(counter);


            }
        };

        timer.schedule(timerTask, 0, 100);

    }

}

