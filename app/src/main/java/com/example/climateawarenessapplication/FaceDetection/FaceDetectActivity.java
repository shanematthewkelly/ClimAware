package com.example.climateawarenessapplication.FaceDetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.climateawarenessapplication.Blog.BlogActivity;
import com.example.climateawarenessapplication.Blog.BotProtectionFrag;
import com.example.climateawarenessapplication.Campaigns.CampaignsAndStatsFragment;
import com.example.climateawarenessapplication.Core.MainActivity;
import com.example.climateawarenessapplication.HomeDashboard.HomeFragment;
import com.example.climateawarenessapplication.Login.LoginFragment;
import com.example.climateawarenessapplication.News.NewsFragment;
import com.example.climateawarenessapplication.Profile.ProfileFragment;
import com.example.climateawarenessapplication.R;
import com.example.climateawarenessapplication.Register.RegisterFragment;
import com.example.climateawarenessapplication.Settings.SettingsFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import dmax.dialog.SpotsDialog;

import static android.hardware.Camera.open;

public class FaceDetectActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CAMERA_PHOTO_REQUEST_CODE = 1;
    private CameraView faceCamera;
    private GraphicOverlay graphicOverlay;
    private Button faceBtn;
    private ImageView passcode, backarrow, camera_front, camera_rear;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle myToggle;
    int MEDIA_TYPE_IMAGE = 1;
    Uri fileUri;

     AlertDialog waitDialog;

    @Override
    protected void onResume() {
        super.onResume();
        faceCamera.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        faceCamera.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_face_detect);

        hideItem();

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myToggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(myToggle);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        myToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //hide action bar
        getSupportActionBar().hide();

        //init views
        faceCamera = findViewById(R.id.face_camera);
        graphicOverlay = findViewById(R.id.graphic_overlay);
        faceBtn = findViewById(R.id.face_button);
        passcode = findViewById(R.id.passcode);
        backarrow = findViewById(R.id.backarrow);
        camera_front = findViewById(R.id.camera_front);

        camera_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FaceDetectActivity.this, "Bug", Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//                startActivityForResult(intent, CAMERA_PHOTO_REQUEST_CODE);
//                faceCamera.start();
//                graphicOverlay.clear();
            }
        });


        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BotProtectionFrag()).commit();
                getSupportActionBar().show();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });

        passcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), FaceDetectActivity.class);
                startActivity(intent);
                finish();
            }
        });

        waitDialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false)
                .build();

        //event
        faceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceCamera.start();
                faceCamera.captureImage();
                graphicOverlay.clear();
            }
        });


        faceCamera.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                waitDialog.show();

                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, faceCamera.getWidth(), faceCamera.getHeight(), false);
                faceCamera.stop();

                faceDetector(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

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




    private void faceDetector(Bitmap bitmap) {

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
                .build();

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);

        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {

                faceProcessor(firebaseVisionFaces);

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FaceDetectActivity.this, "Detection Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void faceProcessor(List<FirebaseVisionFace> firebaseVisionFaces) {

        int count = 0;

        for (FirebaseVisionFace userFace : firebaseVisionFaces) {

            Rect bounds = userFace.getBoundingBox();

            //Draws the rectangles
            FacialRectangle facialRectangle = new FacialRectangle(graphicOverlay, bounds);
            graphicOverlay.add(facialRectangle);

            count++;
        }
        waitDialog.dismiss();

        if (count == 1) {
        Toast.makeText(this, String.format("1 face detected - Please Wait"), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, String.format("Found %d faces - 1 allowed", count), Toast.LENGTH_LONG).show();
        }



        if(count == 0) {
            Toast.makeText(this, "No faces where detected!", Toast.LENGTH_LONG).show();


        } else {

                if (count == 1)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), BlogActivity.class);
                    startActivity(intent);
                    getSupportActionBar().show();
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    finish();
                }
            }, 3000);

        }
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
//              Intent intent = new Intent(this, BlogActivity.class);
//              startActivity(intent);
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
