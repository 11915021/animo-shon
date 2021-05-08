package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Application.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;


public class Login extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private EditText emailAddress2, password2;
    private String parentDbName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_menu);

        initializeUI();

        Paper.init(this);


        FloatingActionButton backButton2 = (FloatingActionButton)findViewById(R.id.backButton2);
        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
            }
        });

        Button loginButton = (Button)findViewById(R.id.login);
        parentDbName = "User";
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parentDbName.equals("User")){
                    loginUserAccount();
                }
            }
        });
    }

    private void loginUserAccount() {
        String email, password;
        email = emailAddress2.getText().toString();
        password = password2.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (email.equals("admin")) {
            if (password.equals("admin")){
                Toast.makeText(getApplicationContext(), "Welcome Admin!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Login.this, AdminMenu.class);
                startActivity(intent);
            }
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(Login.this, MainMenu.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        Paper.book().write(Prevalent.UserEmailKey, email);
        Paper.book().write(Prevalent.UserPasswordKey, password);
    }



    private void initializeUI() {
        emailAddress2 = findViewById(R.id.emailAddress2);
        password2 = findViewById(R.id.password2);
    }
}
