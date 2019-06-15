package com.example.drinkson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class masterpage extends AppCompatActivity {

    private ImageButton your_location;
    private ImageButton rack_number;
    private ImageButton chatroom;
    private ImageButton friend_list;
    private ImageButton roulette;
    private ImageButton shareLocation;
    private Button      logOut;


    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_page);


        // Finds the ID made in the XML file for the this java file.
        logOut        = findViewById(R.id.logOut);
        shareLocation = findViewById(R.id.shareLocation);
        your_location = findViewById(R.id.imagelocation);
        rack_number   = findViewById(R.id.rack);
        chatroom      = findViewById(R.id.imagechatroom);
        friend_list   = findViewById(R.id.friend_lists);
        roulette      = findViewById(R.id.roulette);
        logOut        = findViewById(R.id.logOut);


        // Sets the action when button clicked.
        shareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShareLocation();
            }
        });

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

        roulette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoulette();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogIn();
            }
        });

    }


    public void openYourLocation(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void openRackNumber(){
        Intent intent = new Intent(this, groupmaker.class);
        startActivity(intent);
    }

    public void openChatroom(){
        Intent intent = new Intent(this, ChatRoom.class);
        startActivity(intent);
    }

    public void openFriendList(){
        Intent intent = new Intent(this, Followers.class);
        startActivity(intent);
    }

    public void openRoulette(){
        Intent intent = new Intent(this, Roulette.class);
        startActivity(intent);
    }

    public void openLogIn(){
        Intent intent = new Intent(this, loginpage.class);
        startActivity(intent);
    }

    public void openShareLocation(){
        Intent intent = new Intent(this, locations.class);
        startActivity(intent);
    }

}
