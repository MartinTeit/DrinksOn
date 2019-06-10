package com.example.drinkson;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class groupmaker extends AppCompatActivity {

    Button button;
    Button button2;
    private String   newstring;
    private TextView text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmaker);

        button = (Button) findViewById(R.id.userCreated);
        button2 = (Button) findViewById(R.id.followgroup);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final user newUser = new user();

                final EditText groupBox = (EditText) findViewById(R.id.groupName);

                newUser.id = groupBox.getText().toString();
                newUser.name = "%GRP" + groupBox.getText().toString();
                newUser.stamp = System.currentTimeMillis();

                final follows follows1 = new follows();

                follows1.follower = currentuser.getCurrentUser();
                follows1.followee = groupBox.getText().toString();
                follows1.stamp = System.currentTimeMillis();

                final localdatabase database = Room.databaseBuilder(getApplicationContext(),
                        localdatabase.class, "Danskere").build();
                final DAO dao = database.getDAO();

                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... voids) {
                        dao.insertUser(newUser);
                        dao.insertFollows(follows1);
                        return null;
                    }
                }.execute();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText groupfollowBox = (EditText) findViewById(R.id.groupfollow);

                final follows follows1 = new follows();

                follows1.follower = currentuser.getCurrentUser();
                follows1.followee = groupfollowBox.getText().toString();
                follows1.stamp = System.currentTimeMillis();

                final localdatabase database = Room.databaseBuilder(getApplicationContext(),
                        localdatabase.class, "Danskere").build();
                final DAO dao = database.getDAO();

                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... voids) {
                        dao.insertFollows(follows1);
                        System.out.println("hello");
                        return null;
                    }
                }.execute();
            }
        });

        final localdatabase database = Room.databaseBuilder(getApplicationContext(),
                localdatabase.class, "Danskere").build();
        final DAO dao = database.getDAO();

        text   = findViewById(R.id.name);

        newstring="";
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... voids) {
                for (String string : dao.findFollowees(currentuser.getCurrentUser())){
                    if (dao.findUser(string).name.contains("%GRP")){
                        newstring = newstring + string + " , ";
                        System.out.println("hej");
                    }
                }
                text.setText(newstring);
                return null;
            }

        }.execute();


    }
}
