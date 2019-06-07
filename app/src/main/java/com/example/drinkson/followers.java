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
        try {
            List<fo> allFollowers = new AsyncTask<Void, Void, List<user>>() {

                @Override
                protected List<user> doInBackground(Void... voids) {
                    return database.getDAO().getAll();
                }
            }
        ScrollView scrollView = findViewById(R.id.listOfFollowers);

        LinearLayout usersWhoFollow = new LinearLayout(this);
        usersWhoFollow.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(usersWhoFollow);

        TextView[] userFollowers = new TextView[];

        //for (int users : FOLLOWERS ARRAY ) {
        //    usersWhoFollow[users] = new TextView(this);
        //    usersWhoFollow[users].setText(textLog);
        //    scrollView.addView(usersWhoFollow[INSERT FOLLOWERS]);
        //}
    }


}
