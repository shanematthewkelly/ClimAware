package com.example.climateawarenessapplication.Preloader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.climateawarenessapplication.R;
import com.example.climateawarenessapplication.Introduction.IntroActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Preloader extends AppCompatActivity {

    Timer timer;
    TextView title, desc;
    View divider1,divider2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preloader);

        //Animation init
        Animation TopToBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_to_bottom);
        Animation BottomToTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_to_top);

        //init views
        divider1 = findViewById(R.id.divider);
        divider1.setAnimation(BottomToTop);

        divider2 = findViewById(R.id.divider2);
        divider2.setAnimation(BottomToTop);

        title = findViewById(R.id.climaware_title);
        title.setAnimation(TopToBottom);

        desc = findViewById(R.id.climaware_desc);
        desc.setAnimation(BottomToTop);

        //hide action bar
        getSupportActionBar().hide();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
            }
        }, 7500);
    }
}
