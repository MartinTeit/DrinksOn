package com.example.drinkson;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class registerUser extends AppCompatActivity {

    private Button userCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("hey");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_user);
        final EditText userNameBox = (EditText) findViewById(R.id.createUsername);

        final EditText fullNameBox = (EditText) findViewById(R.id.createPassword);

        userCreated = (Button) findViewById(R.id.userCreated);
        userCreated.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final user newUser = new user();

                newUser.id = userNameBox.getText().toString();
                newUser.name = fullNameBox.getText().toString();
                newUser.stamp = System.currentTimeMillis();

                System.out.println("hey");

                localdatabase database = Room.databaseBuilder(getApplicationContext(),
                        localdatabase.class, "Værdsatte Danskere").build();
                final DAO dao = database.getDAO();

                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... voids) {
                        dao.insertUser(newUser);
                        return null;
                    }
                }.execute();

                openLaunch();
            }
        });

    }
    public void openLaunch(){
        Intent intent = new Intent(this, loginpage.class);
        startActivity(intent);
    }
}
