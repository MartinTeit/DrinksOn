package com.example.drinkson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class loginpage extends AppCompatActivity {

    private Button logIn;
    private Button register;
    private Repository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        repository = new Repository(this);

        final EditText usernameLogIn = findViewById(R.id.username_id);

        //ID finder to href activities
        register = findViewById(R.id.registerUser);
        logIn    = findViewById(R.id.logIn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(usernameLogIn.getText().toString());
            }
        });

    }
    public void openRegister() {
        Intent intent = new Intent(this, registerUser.class);
        startActivity(intent);
    }

    public void openMaster() {
        Intent intent2 = new Intent(this, masterpage.class);
        startActivity(intent2);
    }

    private void login(String userID){

        user u;
        String userString = repository.remoteGetByID(Repository.USERS, userID);
        if (!userString.equals("[]")){
            u = JSONConverter.decodeUser(userString);
            repository.insertUser(u);

            currentuser.setCurrentUser(u.id);
            openMaster();
        }

    }

}
