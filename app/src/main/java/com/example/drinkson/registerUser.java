package com.example.drinkson;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class registerUser extends AppCompatActivity {

    private Button userCreated;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        repository = new Repository(this);

        final EditText userNameBox = (EditText) findViewById(R.id.createUsername);
        final EditText passwordBox = (EditText) findViewById(R.id.createPassword);
        final EditText fullNameBox = (EditText) findViewById(R.id.fullName);

        userCreated = findViewById(R.id.userCreated);

        userCreated.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                final user newUser = new user();

                newUser.id       = userNameBox.getText().toString();
                newUser.name     = fullNameBox.getText().toString();
                newUser.stamp    = System.currentTimeMillis();

                createUser(newUser);
            }
        });

    }
    public void openMaster(){
        Intent intent = new Intent(this, masterpage.class);
        startActivity(intent);
    }

    public void createUser(user u){

        repository.remotePost(Repository.USERS,JSONConverter.encodeUser(u));
        repository.insertUser(u);
        currentuser.setCurrentUser(u.id);
        openMaster();

    }
}
