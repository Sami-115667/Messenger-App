package com.example.smessenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smessenger.ChatView;
import com.example.smessenger.MainActivity;
import com.example.smessenger.R;
import com.example.smessenger.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

    Button signupButton, login;
    CircleImageView profilepic;
    private FirebaseAuth mAuth;
    Uri imageuri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageURI;
    EditText signupName, signupEmail, signupPassword, signupRepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signupButton = findViewById(R.id.signupsignupid);
        signupName = findViewById(R.id.signupnameid);
        signupEmail = findViewById(R.id.signemailid);
        signupPassword = findViewById(R.id.signuppasswordlid);
        signupRepassword = findViewById(R.id.signupreenterpasswordlid);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        login = findViewById(R.id.Signuploginid);
        profilepic = findViewById(R.id.profileimage);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegister();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            imageuri = data.getData();
            profilepic.setImageURI(imageuri);
        }
    }

    private void UserRegister() {
        String email = signupEmail.getText().toString().trim();
        String name = signupName.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String repassword = signupRepassword.getText().toString().trim();

        if (name.isEmpty()) {
            signupName.setError("Enter a name");
            signupName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            signupEmail.setError("Enter an email address");
            signupEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.setError("Enter a valid email address");
            signupEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            signupPassword.setError("Enter a password");
            signupPassword.requestFocus();
            return;
        }
        if (password.length() < 8) {
            signupPassword.setError("Minimum length of a password should be 8");
            signupPassword.requestFocus();
            return;
        }
        if (!password.equals(repassword)) {
            signupRepassword.setError("Password does not match");
            signupRepassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = task.getResult().getUser().getUid();
                            DatabaseReference reference = database.getReference().child("User").child(userId);
                            StorageReference storageReference = storage.getReference().child("Upload").child(userId);

                            if (imageuri != null) {
                                storageReference.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    imageURI = uri.toString();
                                                    Users users = new Users(imageURI, name, email, password, userId);
                                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(SignUp.this, ChatView.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Data Saving Failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        } else {
                                            // Handle image upload failure
                                            Toast.makeText(getApplicationContext(), " " + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                // If user didn't select an image, proceed with saving user data without an image
                                Users users = new Users("", name, email, password, userId);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignUp.this, ChatView.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Data Saving Failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                        } else {
                            // Handle registration failure
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User is already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Registration unsuccessful: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
