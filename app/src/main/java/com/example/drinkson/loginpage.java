package com.example.drinkson;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class loginpage extends AppCompatActivity {

    private Button logIn;
    private Button register;
    private Repository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        repository = new Repository(this);

        System.out.println(repository.remoteGetByID(Repository.USERS,"errewrewerw"));

        final EditText usernameLogIn = findViewById(R.id.username_id);

        //ID finder to href activities
        register = findViewById(R.id.registerUser);
        logIn    = findViewById(R.id.logIn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(usernameLogIn.getText().toString());
            }
        });

    }
    public void openRegister() {
        Intent intent = new Intent(this, registerUser.class);
        startActivity(intent);
    }

    public void openMaster() {
        Intent intent2 = new Intent(this, masterpage.class);
        startActivity(intent2);
    }

    private void login(String userID){
        user u;
        System.out.println(userID);
        String userString = repository.remoteGetByID(Repository.USERS, userID);
        System.out.println(userString);
        if (!userString.equals("[]")){
            u = JSONConverter.decodeUser(userString);
        }



        /*try {
            List<user> allUsers = new AsyncTask<Void, Void, List<user>>() {

                @Override
                protected List<user> doInBackground(Void... voids) {
                    return database.getDAO().getAll();
                }
            }.execute().get();
            for (user users: allUsers) {
                if ((usernameLogIn.getText().toString().equals(users.id)) && (passwordLogIn.getText().toString().equals(users.password))){
                    currentuser.setCurrentUser(users.id);
                    System.out.println(JSONConverter.encodeUser(users));
                    openMaster();
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
    }

}
