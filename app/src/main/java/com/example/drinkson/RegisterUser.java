package com.example.drinkson;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.net.ssl.HttpsURLConnection;

public class RegisterUser extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        repository = new Repository(this);
        Button userCreated = findViewById(R.id.userCreated);

        final EditText userNameBox = findViewById(R.id.createUsername);
        final EditText fullNameBox = findViewById(R.id.fullName);

        userCreated.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                final User newUser = new User();

                newUser.id       = userNameBox.getText().toString();
                newUser.name     = fullNameBox.getText().toString();
                newUser.stamp    = System.currentTimeMillis();

                createUser(newUser);
            }
        });

    }
    public void openMaster(){
        Intent intent = new Intent(this, MasterPage.class);
        startActivity(intent);
    }

    public void createUser(User u){
        int responseCode;

        responseCode = repository.remotePost(Repository.USERS,JSONConverter.encodeUser(u));
        if(responseCode == HttpsURLConnection.HTTP_CONFLICT){
            Toast.makeText(this, "Username taken", Toast.LENGTH_LONG).show();

        } else if (responseCode == HttpsURLConnection.HTTP_CREATED) {
            repository.insertUser(u);
            CurrentUser.setCurrentUser(u.id);
            openMaster();

        } else {
            System.out.println(responseCode);
        }
    }
}
