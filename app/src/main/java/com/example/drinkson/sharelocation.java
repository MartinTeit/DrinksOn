package com.example.drinkson;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class sharelocation extends AppCompatActivity {

    private Button button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharelocation);

        button = findViewById(R.id.button2);


        final EditText target = findViewById(R.id.sharewith);
        final localdatabase database = Room.databaseBuilder(getApplicationContext(),
                localdatabase.class, "Danskere").build();
        final DAO dao = database.getDAO();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final messages location = new messages();

                location.body = "lng = 100" + "ltd =100 ";
                location.receiver = target.toString();
                location.sender = currentuser.getCurrentUser();
                location.stamp = System.currentTimeMillis();

                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... voids) {
                        dao.insertMessage(location);
                        return null;
                    }
                }.execute();
            }
        });
    }
}
