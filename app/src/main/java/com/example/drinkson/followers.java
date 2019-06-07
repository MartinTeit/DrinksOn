package com.example.drinkson;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class followers extends AppCompatActivity {

    private Button follow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        List<String> followers = new ArrayList<>();
        followers.add("Fisse");

        String display = "Hej";
        TextView text = (TextView) findViewById(R.id.my_text_view);
        text.setText(display);
    }


}
