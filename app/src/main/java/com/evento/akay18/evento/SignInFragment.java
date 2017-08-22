package com.evento.akay18.evento;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private FirebaseAuth mAuth;
    private Button mSignInBtn;
    private EditText mEmailField, mPwdField;
    private String email, password;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        //Instantiate views
        mEmailField = view.findViewById(R.id.emailField);
        mPwdField = view.findViewById(R.id.pwdField);
        mSignInBtn = view.findViewById(R.id.signInBtn);

        //Get Fire Auth Instance
        mAuth = FirebaseAuth.getInstance();

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEmailField.getText().toString();
                password = mPwdField.getText().toString();
                signIn(email,password);
            }
        });

        return view;
    }

    protected void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
