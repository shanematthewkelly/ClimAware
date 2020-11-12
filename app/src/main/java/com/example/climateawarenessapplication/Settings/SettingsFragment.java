package com.example.climateawarenessapplication.Settings;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.climateawarenessapplication.Campaigns.CampsDetailFragment4;
import com.example.climateawarenessapplication.Core.MainActivity;
import com.example.climateawarenessapplication.PayPal.DevDonationFragment;
import com.example.climateawarenessapplication.Notifications.ReminderBroadcast;
import com.example.climateawarenessapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Context.ALARM_SERVICE;

public class SettingsFragment extends Fragment {

    private Switch myswitch, myswitchDonate, notificationSwitch;
    private CardView darkModeCard, darkModeCard2, darkModeCard3, darkModeCard4;
    private Button  donateSettings;
    private TextView logoutSettings;
    private FirebaseAuth.AuthStateListener mAuth;
    private Handler mHandler = new Handler();
    private volatile boolean stopThread =false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            getActivity().setTheme(R.style.darktheme);
        }else {
            getActivity().setTheme(R.style.AppTheme);
        }

            View v = inflater.inflate(R.layout.fragment_settings, container, false);



        Animation BottomToTop = AnimationUtils.loadAnimation(getActivity(),R.anim.bottom_to_top);

        darkModeCard = v.findViewById(R.id.darkmode_card);
        darkModeCard.setAnimation(BottomToTop);

        darkModeCard2 = v.findViewById(R.id.darkmode_card2);
        darkModeCard2.setAnimation(BottomToTop);

        darkModeCard3 = v.findViewById(R.id.darkmode_card3);
        darkModeCard3.setAnimation(BottomToTop);

        darkModeCard4 = v.findViewById(R.id.darkmodecard4);
        darkModeCard4.setAnimation(BottomToTop);



        FirebaseListener();

        logoutSettings = v.findViewById(R.id.logoutSettings);
        logoutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

            }
        });

            //init switches
            myswitch = v.findViewById(R.id.myswitch);
            myswitchDonate = v.findViewById(R.id.myswitchDonate);
            notificationSwitch = v.findViewById(R.id.notificationSwitch);

            if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES) {
                myswitch.setChecked(true);
            }
            myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        restartFragment();

                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        restartFragment();
                    }
                }
            });

        myswitchDonate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    Fragment newFragment = new DevDonationFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
            }
        });

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stopThread = false;
                    RunOnNewThread thread = new RunOnNewThread();
                    thread.start();
                    Toast.makeText(getActivity(), "Notifications On", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getActivity(), "Notifications Off", Toast.LENGTH_SHORT).show();
                    stopThread = true;
                }
            }
        });


            return v;
    }

    private void restartFragment() {

        SettingsFragment settingsFragment = new SettingsFragment();
        FragmentManager fragmentManager4 = getFragmentManager();
        FragmentTransaction fragmentTransaction4  = fragmentManager4.beginTransaction();
        fragmentTransaction4.replace(R.id.fragment_container, settingsFragment);
        fragmentTransaction4.addToBackStack(null);
        fragmentTransaction4.commit();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ClimAwareChannel";
            String description = "Channel for ClimAware Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notify", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }




    private void FirebaseListener() {
        mAuth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    //nothing needs to happen here as we should be signed in by this stage
                }else {
                    Toast.makeText(getActivity(), "Signed Out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuth);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuth != null ) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuth);
        }

    }

    class RunOnNewThread extends Thread {

        @Override
        public void run() {

            if (stopThread)
                return;

            createNotificationChannel();

            Intent intent = new Intent(getActivity(), ReminderBroadcast.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

            AlarmManager alarmManager =  (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

            long timeBtnClicked = System.currentTimeMillis();

            long timeUntilNextNotification = 200 * 10;

            alarmManager.set(AlarmManager.RTC_WAKEUP, timeBtnClicked + timeUntilNextNotification, pendingIntent);

            mHandler.postDelayed(this, 5000);
        }
    }


}
