package com.example.drinkson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class createuser extends AppCompatActivity {

    private Button userCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        userCreated = (Button) findViewById(R.id.userCreated);
        userCreated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLaunch();
            }
        });

    }
    public void openLaunch(){
        Intent intent = new Intent(this, loginpage.class);
        startActivity(intent);
    }
}
