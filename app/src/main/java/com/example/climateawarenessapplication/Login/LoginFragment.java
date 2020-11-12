package com.example.climateawarenessapplication.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.climateawarenessapplication.HomeDashboard.HomeFragment;
import com.example.climateawarenessapplication.Core.LockDrawerInterface;
import com.example.climateawarenessapplication.Core.MainActivity;
import com.example.climateawarenessapplication.R;
import com.example.climateawarenessapplication.Register.RegisterFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment implements View.OnClickListener{


    private EditText userEmail, userPassword;
    private FirebaseAuth myAuth;
    private TextView loginButton, goToRegisterBtn;
    private Intent homeScreen;
    ConstraintLayout loginconstaint1, loginconstaint2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //make the activity on full screen
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        View v = inflater.inflate(R.layout.login_fragment_new, container, false);

        //animation setup
        Animation TopToBottom = AnimationUtils.loadAnimation(getActivity(),R.anim.top_to_bottom);
        Animation BottomToTop = AnimationUtils.loadAnimation(getActivity(),R.anim.bottom_to_top);

        //locks the drawer
        ((LockDrawerInterface)getActivity()).setDrawerLocked(true);

        //init views
        userEmail = v.findViewById(R.id.login_mail);
        userPassword = v.findViewById(R.id.login_password);
        myAuth = FirebaseAuth.getInstance();
        homeScreen = new Intent(getActivity(), MainActivity.class);

         loginButton = v.findViewById(R.id.loginButton);
         loginButton.setOnClickListener(this);

        loginconstaint1 = v.findViewById(R.id.loginconstaint1);
        loginconstaint1.setAnimation(TopToBottom);

        loginconstaint2 = v.findViewById(R.id.loginconstaint2);
        loginconstaint2.setAnimation(BottomToTop);



        goToRegisterBtn = v.findViewById(R.id.joinUsButton);
        goToRegisterBtn.setOnClickListener(this);



        return v;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.loginButton:
                loginButton.setVisibility(View.INVISIBLE);

                closeKeyboard();


                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    showMessage("Please fill in the details above");
                    loginButton.setVisibility(View.VISIBLE);


                } else {
                    signIn(email, password);
                }


                break;
            case R.id.joinUsButton:
                RegisterFragment registerFragment = new RegisterFragment();
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2  = fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.fragment_container, registerFragment);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
                break;

        }
    }

    private void closeKeyboard() {

      View view = getActivity().getCurrentFocus();
      if (view != null) {
          InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
      }
    }

    private void signIn(String email,final String password) {

        myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    loginButton.setVisibility(View.VISIBLE);
                    updateUI();
                    Toast.makeText(getActivity(), "Logged In!", Toast.LENGTH_SHORT).show();

                } else {
                    showMessage(task.getException().getMessage());
                    loginButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Incorrect email or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateUI() {

        Fragment newFragment = new HomeFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser user = myAuth.getCurrentUser();
//
//        if (user != null) {
//
//            //user is already connected/signed in so we redirect them to the home screen (ListActivity)
//            //runs the updatedUI function
//            updateUI();
//
//        }
//    }


    private void showMessage(String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

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
    public void onDestroyView() {
        super.onDestroyView();

        ((LockDrawerInterface)getActivity()).setDrawerLocked(false);
    }
}
