package com.example.climateawarenessapplication.Profile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.climateawarenessapplication.Campaigns.CampaignsAndStatsFragment;
import com.example.climateawarenessapplication.Login.LoginFragment;
import com.example.climateawarenessapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;


public class UpdateProfileFragment extends Fragment {


    private EditText editUsername;
    private ImageView editprofileImage;
    private Button updateProfileBtn;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    CardView bigCard;
    static int PReqCode = 100;
    static int REQUESTCODE = 200;
    Uri pickedImgUri;
    String DISPLAY_NAME = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_update_profile, container, false);

        //setup Animation
        Animation BottomToTop = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_to_top);

        //init views
        updateProfileBtn = v.findViewById(R.id.updateProfileBtn);
        editUsername = v.findViewById(R.id.editUsername);
        updateProfileBtn = v.findViewById(R.id.updateProfileBtn);
        editprofileImage = v.findViewById(R.id.editprofileImage);
        bigCard = v.findViewById(R.id.bigCard);
        bigCard.setAnimation(BottomToTop);

        editprofileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermissions();

                } else {

                    openGallery();
                }

            }

        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DISPLAY_NAME = editUsername.getText().toString();

                FirebaseUser user = mAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                        .setDisplayName(DISPLAY_NAME)
                        .build();

                user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getActivity(), "Profile Updated!", Toast.LENGTH_SHORT).show();

                        ProfileFragment profileFragment = new ProfileFragment();
                        FragmentManager fragmentManager4 = getFragmentManager();
                        FragmentTransaction fragmentTransaction4  = fragmentManager4.beginTransaction();
                        fragmentTransaction4.replace(R.id.fragment_container, profileFragment);
                        fragmentTransaction4.addToBackStack(null);
                        fragmentTransaction4.commit();


                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(), "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                });

                updateUserInfo(pickedImgUri, mAuth.getCurrentUser());

            }
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("Users");

        FirebaseUser user = mAuth.getInstance().getCurrentUser();

        if (user != null) {

            if (user.getDisplayName() != null) {

                editUsername.setText(user.getDisplayName());
                editUsername.setSelection(user.getDisplayName().length());
            }
                if (user.getPhotoUrl() != null) {
                    Glide.with(getActivity())
                            .load(user.getPhotoUrl())
                            .into(editprofileImage);
               }

        }



        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            editprofileImage.setImageURI(pickedImgUri);


        }


    }

    private void updateUserInfo(Uri pickedImgUri, final FirebaseUser currentUser) {

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //image uploaded successfully
                //we can get our image url
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {

                        //uri contains the user's image uri
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    //user info updated successfully
                                    Toast.makeText(getActivity(), "Please refresh", Toast.LENGTH_LONG).show();


                                } else {
                                    //user info failed to update
                                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                    }
                });

            }
        });

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);

    }

    private void checkAndRequestForPermissions() {


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(getActivity(), "Please Accept For Required Permissions", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            openGallery();

    }
}
