package com.example.drinkson;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class registerUser extends AppCompatActivity {

    private Button userCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_user);

        final EditText userNameBox = (EditText) findViewById(R.id.createUsername);
        final EditText passwordBox = (EditText) findViewById(R.id.createPassword);
        final EditText fullNameBox = (EditText) findViewById(R.id.fullName);

        userCreated = (Button) findViewById(R.id.userCreated);
        userCreated.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                    final user newUser = new user();

                    newUser.id       = userNameBox.getText().toString();
                    newUser.password = passwordBox.getText().toString();
                    newUser.name     = fullNameBox.getText().toString();
                    newUser.stamp    = System.currentTimeMillis();

                    final localdatabase database = Room.databaseBuilder(getApplicationContext(),
                            localdatabase.class, "Danskere").build();
                    final DAO dao = database.getDAO();

                    new AsyncTask<Void, Void, Void>() {
                        protected Void doInBackground(Void... voids) {
                            dao.insertUser(newUser);
                            return null;
                        }
                    }.execute();

                    try {
                        List<user> allUsers = new AsyncTask<Void, Void, List<user>>() {

                            @Override
                            protected List<user> doInBackground(Void... voids) {
                                return database.getDAO().getAll();
                            }
                        }.execute().get();
                        for (user users : allUsers) {
                            Log.d("Request", users.id + " - " + users.password);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    openLaunch();
             }
        });

    }
    public void openLaunch(){
        Intent intent = new Intent(this, loginpage.class);
        startActivity(intent);
    }
}
