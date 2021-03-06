package com.example.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.Application.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private EditText fullNameEdit, addressEdit, phoneNumberEdit, cityEdit;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullNameEdit = (EditText) findViewById(R.id.settings_full_name);
        addressEdit = (EditText) findViewById(R.id.settings_address);
        phoneNumberEdit = (EditText) findViewById(R.id.settings_phone_number);
        cityEdit = (EditText) findViewById(R.id.settings_city);
        updateButton = (Button) findViewById(R.id.updateProfileBtn);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getStudentnumber());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fullNameEdit.setText(dataSnapshot.child("name").getValue(String.class));
                addressEdit.setText(dataSnapshot.child("address").getValue(String.class));
                phoneNumberEdit.setText(dataSnapshot.child("phonenumber").getValue(String.class));
                cityEdit.setText(dataSnapshot.child("city").getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        ImageButton backButton = (ImageButton)findViewById(R.id.profile_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, Me.class);
                startActivity(i);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });




    }

    private void updateUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", fullNameEdit.getText().toString());
        userMap.put("address", addressEdit.getText().toString());
        userMap.put("phonenumber", phoneNumberEdit.getText().toString());
        userMap.put("city", cityEdit.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getStudentnumber()).updateChildren(userMap);

        startActivity(new Intent(ProfileActivity.this, Me.class));
        Toast.makeText(ProfileActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
    }


}