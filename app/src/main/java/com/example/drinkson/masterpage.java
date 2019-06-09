package com.example.drinkson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class masterpage extends AppCompatActivity {

    private ImageButton your_location;
    private ImageButton rack_number;
    private ImageButton chatroom;
    private ImageButton friend_list;

    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_page);

        your_location = findViewById(R.id.imagelocation);
        rack_number   = findViewById(R.id.rack);
        chatroom      = findViewById(R.id.imagechatroom);
        friend_list   = findViewById(R.id.friend_lists);

        your_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYourLocation();
            }
        });

        rack_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRackNumber();
            }
        });

        chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatroom();
            }
        });

        friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriendList();
            }
        });

    }
    public void openYourLocation(){
        Intent intent = new Intent(this, location.class);
        startActivity(intent);
    }

    public void openRackNumber(){
        Intent intent = new Intent(this, groupmaker.class);
        startActivity(intent);
    }

    public void openChatroom(){
        Intent intent = new Intent(this, chatroom.class);
        startActivity(intent);
    }

    public void openFriendList(){
        Intent intent = new Intent(this, followers.class);
        startActivity(intent);
    }

}
