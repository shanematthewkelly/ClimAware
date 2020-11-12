package com.example.climateawarenessapplication.Biometrics;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.biometric.BiometricManager;
import android.content.Context;
import android.content.Intent;
import androidx.biometric.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.climateawarenessapplication.Blog.BotProtectionFrag;
import com.example.climateawarenessapplication.Campaigns.CampaignsAndStatsFragment;
import com.example.climateawarenessapplication.Campaigns.CampaignsFragment;
import com.example.climateawarenessapplication.Core.MainActivity;
import com.example.climateawarenessapplication.HomeDashboard.HomeFragment;
import com.example.climateawarenessapplication.Login.LoginFragment;
import com.example.climateawarenessapplication.News.NewsFragment;
import com.example.climateawarenessapplication.Passcode.PasscodeActivity;
import com.example.climateawarenessapplication.PayPal.PayPalActivity;
import com.example.climateawarenessapplication.Profile.ProfileFragment;
import com.example.climateawarenessapplication.R;
import com.example.climateawarenessapplication.Register.RegisterFragment;
import com.example.climateawarenessapplication.Settings.SettingsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;
import java.util.concurrent.Executor;


public class Biometrics extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Context context = this;
    private ConstraintLayout mainConst;
    private TextView startPasscodeProcess;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle myToggle;
    private ImageView backarrow2;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_finger_auth);

        hideItem();


        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        myToggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(myToggle);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        myToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        backarrow2 = findViewById(R.id.backarrow2);

        backarrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainConst.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CampaignsFragment()).commit();
                getSupportActionBar().show();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            }
        });

        //hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        Animation SmallToBig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);

        mainConst = findViewById(R.id.fingerprintCanvas);
        mainConst.setAnimation(SmallToBig);
        startPasscodeProcess = findViewById(R.id.usepasscode);

        //Biometric Manager
        BiometricManager biometricManager = BiometricManager.from(context);

        //Device Compatibility check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switch (biometricManager.canAuthenticate()) {

                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    Snackbar.make(mainConst, "Unfortunately, the fingerprint is not available right now.", 4000)
                            .setActionTextColor(getResources().getColor(R.color.white))
                            .show();
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    Snackbar.make(mainConst, "Please make sure to register a fingerprint on your device first.", 5000)
                            .setActionTextColor(getResources().getColor(R.color.white))
                            .show();
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    Snackbar.make(mainConst, "Oh no! It looks like you don't have a fingerprint on your device. Please use our passcode instead.", 8000)
                            .setActionTextColor(getResources().getColor(R.color.white))
                            .show();
                    break;
                case BiometricManager.BIOMETRIC_SUCCESS:
                    break;
            }
        }

        //Execute the process
        Executor executor = ContextCompat.getMainExecutor(context);

        //Biometric prompt callback & result of the authentication (Allowed/Not Allowed)
        final BiometricPrompt biometricPrompt = new BiometricPrompt(Biometrics.this, executor, new BiometricPrompt.AuthenticationCallback() {

            //Authentication Error
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Snackbar.make(mainConst, "We could not log you in just now!", 3000)
                        .setActionTextColor(getResources().getColor(R.color.white))
                        .show();            }

            //Authentication Success
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                //Allow the user to proceed to the application
                Intent intent = new Intent(context, PayPalActivity.class);
                startActivity(intent);
                finish();
            }

            //Authentication Failed
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }

        });

        //Dialog Box Prompt
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Secure Biometric Login")
                .setDescription("Please use the following login methods")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);



        //Go to passcode activity instead
        startPasscodeProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startPasscodeActivity = new Intent(context, PasscodeActivity.class);
                startActivity(startPasscodeActivity);
                finish();
            }
        });
    }


    private void hideItem() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.login).setVisible(false);
        nav_Menu.findItem(R.id.register).setVisible(false);
    }

    //Opens up the navgation bar by clicking on the burger
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (myToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.news:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsFragment()).commit();
                break;
            case R.id.campaigns:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CampaignsAndStatsFragment()).commit();
                break;
            case R.id.blog:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BotProtectionFrag()).commit();
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                break;
            case R.id.register:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegisterFragment()).commit();
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.logout:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


}

