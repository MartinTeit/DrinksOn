package com.example.drinkson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class chatroom extends AppCompatActivity {

    private Button someButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);


        someButton = findViewById(R.id.aButton);

        someButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });


    }


    public void openChat(){
        Intent intent = new Intent(this, chat.class);
        startActivity(intent);
    }
}
