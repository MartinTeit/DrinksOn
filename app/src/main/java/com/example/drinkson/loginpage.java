package com.example.drinkson;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class loginpage extends AppCompatActivity {

    private Button logIn;
    private Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        final EditText usernameLogIn = (EditText) findViewById(R.id.username_id);

        final localdatabase database = Room.databaseBuilder(getApplicationContext(),
                localdatabase.class, "Værdsatte Danskere").build();
        final DAO dao = database.getDAO();


        //ID finder to href activities
        register = (Button) findViewById(R.id.registerUser);
        logIn = (Button) findViewById(R.id.logIn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    List<user> allUsers = new AsyncTask<Void, Void, List<user>>() {

                        @Override
                        protected List<user> doInBackground(Void... voids) {
                            return database.getDAO().getAll();
                        }
                    }.execute().get();
                    for (user users: allUsers) {
                        if (usernameLogIn.getText().toString().equals(users.id)){
                         openMaster();
                         break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
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

}
