package com.example.climateawarenessapplication.Register;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.climateawarenessapplication.Core.LockDrawerInterface;
import com.example.climateawarenessapplication.Login.LoginFragment;
import com.example.climateawarenessapplication.Core.MainActivity;
import com.example.climateawarenessapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    private EditText userEmail, userPassword, myphone, userName;
    private Button registerButton;
    private FirebaseAuth myAuth;
    private TextView alreadyRegistered;
    private CardView registerForm;
    private CircularImageView profilePic;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    private FirebaseFirestore fStore;
    String userID;
    Uri pickedImgUri;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        //animation setup
        Animation TopToBottom = AnimationUtils.loadAnimation(getActivity(),R.anim.top_to_bottom);

        //locks the drawer
        ((LockDrawerInterface)getActivity()).setDrawerLocked(true);

        //init views
        userEmail = v.findViewById(R.id.regEmail);
        userName = v.findViewById(R.id.regName);
        userPassword = v.findViewById(R.id.regPassword);
        myphone = v.findViewById(R.id.phone);

        myAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        registerForm = v.findViewById(R.id.registerForm);
        registerForm.setAnimation(TopToBottom);


        registerButton = v.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        alreadyRegistered = v.findViewById(R.id.alreadyRegistered);
        alreadyRegistered.setOnClickListener(this);

        profilePic = v.findViewById(R.id.profilepic);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermissions();

                } else {

                    openGallery();
                }

            }
        });

        return v;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            profilePic.setImageURI(pickedImgUri);


        }


    }




    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.registerButton:
                //sets the register button to be invisible when clicked
                registerButton.setVisibility(View.INVISIBLE);

                closeKeyboard();

                //gets the EditTexts and converts them to a string
                 String email = userEmail.getText().toString();
                 String username = userName.getText().toString();
                 String password = userPassword.getText().toString();
                 String phone = myphone.getText().toString();


                if (email.isEmpty() || username.isEmpty() || password.isEmpty() || phone.isEmpty()) {

                    //if something goes wrong: all fields must be filled, we handle the error message in here
                    showMessage("Please fill in the required fields above");
                    registerButton.setVisibility(View.VISIBLE);


                } else {
                    //everything is okay and all fields are filled, we can proceed with making an account

                    //CreateUserAccount method will create the user if the email is valid
                    CreateUserAccount(email, username, password, phone);
                }

                break;

            case R.id.alreadyRegistered:
                LoginFragment loginFragment2 = new LoginFragment();
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2  = fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.fragment_container, loginFragment2);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
                break;

        }


    }

    private void CreateUserAccount(String email, final String username, String password, final String phone) {

        myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    //user account created successfully
                    showMessage("Account Created!");
                    //after we created the user account we need to update their name
                    updateUserInfo(username,pickedImgUri, myAuth.getCurrentUser());

                    String user_id = myAuth.getCurrentUser().getUid();
                    DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                    String phone = myphone.getText().toString();
                    String email = userEmail.getText().toString();

                    Map newProfile = new HashMap();
                    newProfile.put("email", email);
                    newProfile.put("phone", phone);

                    current_user.setValue(newProfile);

                } else {
                    //account creation failed
                    showMessage("Account Creation Failed..." + task.getException().getMessage());
                    registerButton.setVisibility(View.VISIBLE);
                }
            }
        });

    }



    private void updateUserInfo(final String username, Uri pickedImgUri, final FirebaseUser currentUser) {

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
                                .setDisplayName(username)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    //user info updated successfully
                                    showMessage("Registration Complete!");
                                    updateUI();

                                } else {
                                    //user info failed to update
                                    showMessage("Registration failed...");
                                }


                            }
                        });

                    }
                });



            }
        });



    }

    private void updateUI() {


        Intent loginpage = new Intent(getActivity(), MainActivity.class);
        startActivity(loginpage);
        getActivity().finish();

    }


    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((LockDrawerInterface)getActivity()).setDrawerLocked(false);
    }

    private void closeKeyboard() {

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
