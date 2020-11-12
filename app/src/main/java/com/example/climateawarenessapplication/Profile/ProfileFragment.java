package com.example.climateawarenessapplication.Profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
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
import com.example.climateawarenessapplication.Campaigns.CampsDetailFragment4;
import com.example.climateawarenessapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.mikhaellopez.circularimageview.CircularImageView;


public class ProfileFragment extends Fragment {

    TextView myUsername, profilemyemail, Profilephone;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    String userID;
    Button editProfileBtn;
    ImageView refresher;
    CardView bigCard;
    CircularImageView profilepageimage;
    FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Animation BigToSmall = AnimationUtils.loadAnimation(getActivity(), R.anim.smalltobig);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("Users");
        firebaseUser = mAuth.getCurrentUser();

        editProfileBtn = v.findViewById(R.id.editProfileBtn);
        bigCard = v.findViewById(R.id.bigCard);
        bigCard.setAnimation(BigToSmall);
        refresher = v.findViewById(R.id.refresher);

        refresher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileFragment profileFragment = new ProfileFragment();
                FragmentManager fragmentManager4 = getFragmentManager();
                FragmentTransaction fragmentTransaction4  = fragmentManager4.beginTransaction();
                fragmentTransaction4.replace(R.id.fragment_container, profileFragment);
                fragmentTransaction4.addToBackStack(null);
                fragmentTransaction4.commit();
            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateProfileFragment updateProfileFragment = new UpdateProfileFragment();
                FragmentManager fragmentManager4 = getFragmentManager();
                FragmentTransaction fragmentTransaction4  = fragmentManager4.beginTransaction();
                fragmentTransaction4.replace(R.id.fragment_container, updateProfileFragment);
                fragmentTransaction4.addToBackStack(null);
                fragmentTransaction4.commit();
            }
        });

        profilepageimage = v.findViewById(R.id.profilepageimage);
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(profilepageimage);
        myUsername = v.findViewById(R.id.profileusername);
        profilemyemail =v.findViewById(R.id.profilemyemail);
        Profilephone = v.findViewById(R.id.profilephone);
        userID = firebaseUser.getUid();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                }
            }
        };
        Query query = mRef.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

             String Phone = ""+ ds.child("phone").getValue();

             Profilephone.setText(Phone);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (firebaseUser != null) {

            myUsername.setText(firebaseUser.getDisplayName());
            profilemyemail.setText(firebaseUser.getEmail());
//            profilephone.setText(user.getPhoneNumber());

        }


        return v;

    }

}
