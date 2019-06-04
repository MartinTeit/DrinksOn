package com.example.drinkson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class registerUser extends AppCompatActivity {

    private Button userCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_user);
        final EditText userNameBox = (EditText) findViewById(R.id.createUsername);

        final EditText fullNameBox = (EditText) findViewById(R.id.createPassword);

        userCreated = (Button) findViewById(R.id.userCreated);
        userCreated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user newUser = new user();
                newUser.id = userNameBox.getText().toString();
                newUser.name = fullNameBox.getText().toString();
                //System.out.println(fullNameBox.getText().toString());
                openLaunch();
            }
        });

    }
    public void openLaunch(){
        Intent intent = new Intent(this, loginpage.class);
        startActivity(intent);
    }
}
