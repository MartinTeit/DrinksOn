package com.example.drinkson;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class followers extends AppCompatActivity {

    private Button follow;
    private TextView text;
    private String newstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        final EditText followerUsername = findViewById(R.id.followerUsername);

        final localdatabase database = Room.databaseBuilder(getApplicationContext(),
                localdatabase.class, "Danskere").build();
        final DAO dao = database.getDAO();

        follow = findViewById(R.id.button);

        text = findViewById(R.id.name);
        text.setText("fisk");

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final follows follows1 = new follows();

                follows1.follower = currentuser.getCurrentUser();
                follows1.followee = followerUsername.getText().toString();
                follows1.stamp = System.currentTimeMillis();

                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... voids) {
                        dao.insertFollows(follows1);
                        return null;
                    }
                }.execute();
            }
        });


        newstring = "";
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... voids) {
                for (String string : dao.findFollowers(currentuser.getCurrentUser())){
                    newstring = newstring + string + "\n";
                }

                text.setText(newstring);
                return null;
            }

        }.execute();

    }

}
