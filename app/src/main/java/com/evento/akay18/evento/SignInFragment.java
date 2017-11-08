package com.evento.akay18.evento;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button mSignInBtn;
    private EditText mEmailField, mPwdField;
    private String email, password;
    private View focusView = null;
    private boolean notEmpty = true, emailVerified;
    private ConnectivityManager mConnMgr;
    private NetworkReceiver mReceiver;
    private TextInputLayout emailIL, pwdIL;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        //This ConnectivityManager reference is used to check connectivity throughout the app.
        mConnMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        //Instantiate Broadcast receiver.
        mReceiver = new NetworkReceiver();

        //Intent Filter will receive System Broadcast.
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        //Register Broadcast receiver with the filter.
        //So that whenever a change is made in network onReceive method is called.
        getActivity().registerReceiver(mReceiver, filter);

        //Instantiate views
        mEmailField = view.findViewById(R.id.emailField);
        mPwdField = view.findViewById(R.id.PwdField);
        mSignInBtn = view.findViewById(R.id.signInBtn);
        emailIL = view.findViewById(R.id.emailInputLayout);
        pwdIL = view.findViewById(R.id.pwdInputLayout);

        //Get Fire Auth Instance
        mAuth = FirebaseAuth.getInstance();

        //On click sign in button
        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mConnMgr != null) {
                    NetworkInfo networkInfo = mConnMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        email = mEmailField.getText().toString();
                        checkEmail();
                        password = mPwdField.getText().toString();
                        checkPassword();
                        if (notEmpty) {
                            signIn(email, password);
                            isEmailVerified();
                        }
                    } else {
                        Snackbar.make(getView(), "Please connect to the Internet", Snackbar.LENGTH_LONG).show();
                    }

                }
            }
        });
        return view;
    }

    //Email Verification
    protected void isEmailVerified(){
        mUser = mAuth.getCurrentUser();
        if(mUser != null){
            if(mUser.isEmailVerified()){
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            } else {
                Snackbar.make(getView(), "Please verify your email first!", Snackbar.LENGTH_SHORT).show();
                mAuth.signOut();
            }
        }
    }

    //Password validation
    protected void checkPassword(){
        if (TextUtils.isEmpty(password)) {
            pwdIL.setError("Password Required");
            notEmpty = false;
        }else {
            notEmpty = true;
            pwdIL.setError(null);
        }
    }

    //Email validation
    protected void checkEmail() {
        if (TextUtils.isEmpty(email)) {
            emailIL.setError("Email Required");
            //mEmailField.setError("Email Required");
            focusView = mEmailField;
            notEmpty = false;
        } else if (!isEmailVerified(email)) {
            emailIL.setError("Email not valid");
            //mEmailField.setError("Email not valid");
            focusView = mEmailField;
            notEmpty = false;
        } else{
            notEmpty = true;
            emailIL.setError(null);
        }
    }

    protected boolean isEmailVerified(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    protected void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void autoSignIn(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("At autoSignIn", "Successful");
                }
            }
        });
    }

    //Check network status at RUNTIME using BroadcastReceiver.
    //onReceive method will be called whenever network changes.
    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //Get Active network status.
            NetworkInfo networkInfo = mConnMgr.getActiveNetworkInfo();

            if (networkInfo == null && !networkInfo.isConnected()) {

                Snackbar.make(getView(), "Please connect to the Internet", Snackbar.LENGTH_LONG).show();

                /*//Check if active network interface is wifi.
                boolean isWifiAvailable = mConnMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();

                //Check if active network interface is gsm.
                boolean isGsmAvailable = mConnMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();

                if ((!isWifiAvailable) && (!isGsmAvailable)) {
                    Snackbar.make(getView(), "Please connect to the Internet", Snackbar.LENGTH_LONG).show();
                }*/
            }

        }
    }

}
