package com.example.smessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button signinbutton,loginbutton;
    EditText signinEmail,signinPassword;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signinbutton=findViewById(R.id.loginsigninid);
        loginbutton=findViewById(R.id.loginloginid);
        mAuth=FirebaseAuth.getInstance();
        signinEmail=findViewById(R.id.loginemailid);
        signinPassword=findViewById(R.id.loginpasswordid);

        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });



        if(mAuth.getCurrentUser()!=null){
            Intent intent=new Intent(MainActivity.this,ChatView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLogin();
            }
        });
    }



    private void UserLogin() {
        String email=signinEmail.getText().toString().trim();

        String password=signinPassword.getText().toString().trim();


        if(email.isEmpty()){
            signinEmail.setError("Enter an email address");
            signinEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signinEmail.setError("Enter a valid email address");
            signinEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            signinPassword.setError("Enter a password");
            signinPassword.requestFocus();
            return;
        }
        if(password.length()<8){
            signinPassword.setError("Minimum length of a password should be 8");
            signinPassword.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Login Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,ChatView.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Login Unsuccessfully",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}