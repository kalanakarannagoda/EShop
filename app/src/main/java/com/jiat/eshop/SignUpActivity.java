package com.jiat.eshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jiat.eshop.model.User;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "eshop";
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private EditText nameEdit, emailEdit, passwordEdit, retypeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_EShop_FullScreen);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        nameEdit = findViewById(R.id.signup_name);
        emailEdit = findViewById(R.id.signup_email);
        passwordEdit = findViewById(R.id.signup_password);
        retypeEdit = findViewById(R.id.signup_retype_password);


        findViewById(R.id.signup_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.signup_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String reTypePass = retypeEdit.getText().toString();

                if (password.length() >= 8) {

                    if (password.equals(reTypePass)) {

                        view.setEnabled(false);

                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            FirebaseUser firebaseUser = task.getResult().getUser();
                                            User user = new User(firebaseUser.getUid(), name, firebaseUser.getEmail());
                                            firebaseFirestore.collection("users").add(user)
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                               if (task.isSuccessful()){
                                                                   Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                                   startActivity(intent);
                                                                   finish();
                                                               }else {
                                                                   Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                               }
                                                        }
                                                    });

                                        } else {
                                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        view.setEnabled(true);
                                    }
                                });


                    } else {
                        retypeEdit.setError("Password doesn't match!");
                    }

                } else {
                    passwordEdit.setError("Password must contain at least 8 characters");
                }
            }
        });


    }


}