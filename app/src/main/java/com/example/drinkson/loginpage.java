package com.example.drinkson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class loginpage extends AppCompatActivity {

    private Button log;
    private Button register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        //ID finder to href activities
        register = (Button) findViewById(R.id.registerUser);
        log = (Button) findViewById(R.id.logIn);

        //Makes an action
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMaster();
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

}
