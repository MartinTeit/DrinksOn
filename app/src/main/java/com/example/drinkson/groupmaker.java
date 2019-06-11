package com.example.drinkson;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javax.net.ssl.HttpsURLConnection;

public class groupmaker extends AppCompatActivity {

    private Button createGroupButton;
    private Button followGroupButton;
    private String newstring;
    private TextView text;
    private Repository repository;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmaker);

        repository = new Repository(this);

        createGroupButton = (Button) findViewById(R.id.userCreated);
        followGroupButton = (Button) findViewById(R.id.followgroup);

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user newGroup = new user();
                EditText groupBox = (EditText) findViewById(R.id.groupName);

                newGroup.id = groupBox.getText().toString();
                newGroup.name = "%GRP " + groupBox.getText().toString();
                newGroup.stamp = System.currentTimeMillis();

                createGroup(newGroup);
            }
        });

        followGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText groupfollowBox = (EditText) findViewById(R.id.groupfollow);

                follows follows1 = new follows();

                follows1.follower = currentuser.getCurrentUser();
                follows1.followee = groupfollowBox.getText().toString();
                follows1.stamp = System.currentTimeMillis();

                followGroup(follows1);
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
                        newstring = newstring + "%GRP " + string + " , ";
                        System.out.println();
                    }
                }
                text.setText(newstring);
                return null;
            }

        }.execute();

    }
    
    private void createGroup(user newGroup){
        int responseCode;
        follows follows1 = new follows();

        responseCode = repository.remotePost(Repository.USERS,JSONConverter.encodeUser(newGroup));

        if (responseCode != HttpsURLConnection.HTTP_CONFLICT) {
            repository.insertUser(newGroup);

            follows1.follower = currentuser.getCurrentUser();
            follows1.followee = newGroup.id;
            follows1.stamp = System.currentTimeMillis();

            followGroup(follows1);
        }
    }

    private void followGroup(follows newFollows){
        int responseCode;

        responseCode = repository.remotePost(Repository.FOLLOWS,JSONConverter.encodeFollows(newFollows));

        if (responseCode != HttpsURLConnection.HTTP_CONFLICT) {
            repository.insertFollows(newFollows);
        }
    }
    
    
}
