package com.example.drinkson;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class GroupMaker extends AppCompatActivity {

    private TextView text;
    private Repository repository;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmaker);

        repository = new Repository(this);

        Button createGroupButton = findViewById(R.id.userCreated);
        Button followGroupButton = findViewById(R.id.followgroup);

        // When create group button is pushed create a group and follow it
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User newGroup = new User();
                EditText groupBox = findViewById(R.id.groupName);

                newGroup.id = groupBox.getText().toString();
                newGroup.name = "%GRP " + groupBox.getText().toString();
                newGroup.stamp = System.currentTimeMillis();

                createGroup(newGroup);
            }
        });

        // When follow group button is pushed follow the group
        followGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText groupFollowBox = findViewById(R.id.groupfollow);

                Follows follows1 = new Follows();

                follows1.follower = CurrentUser.getCurrentUser();
                follows1.followee = groupFollowBox.getText().toString();
                follows1.stamp = System.currentTimeMillis();

                followGroup(follows1);
            }
        });

        text   = findViewById(R.id.name);

        updateGroups();

    }

    // Take a group an post it to the remote database, if the request returns "HTTP_CREATED",
    // insert the new group in the local database and follow thw group automatically
    private void createGroup(User newGroup){
        int responseCode;
        Follows newFollows = new Follows();

        responseCode = repository.remotePost(Repository.USERS,JSONConverter.encodeUser(newGroup));
        if (responseCode == HttpsURLConnection.HTTP_CREATED) {
            repository.insertUser(newGroup);

            newFollows.follower = CurrentUser.getCurrentUser();
            newFollows.followee = newGroup.id;
            newFollows.stamp = System.currentTimeMillis();

            followGroup(newFollows);
        }
    }

    // Takes a follows and post it to the remote database.
    // if the request returned "HTTP_CREATED" insert the follows locally
    private void followGroup(Follows newFollows){
        int responseCode;

        responseCode = repository.remotePost(Repository.FOLLOWS,JSONConverter.encodeFollows(newFollows));
        if (responseCode == HttpsURLConnection.HTTP_CREATED) {
            repository.insertFollows(newFollows);
        }

        updateGroups();
    }

    // Update the list containing followed groups
    private void updateGroups(){
        String newString="";
        // Get all the follows from the room database
        List<Follows> followedGroups = repository.getAllMyFollowees();

        for (Follows fg : followedGroups){
            // If the name of the followee starts with "%GRP" it is a group
            // and is showed on the list
            if (repository.findUser(fg.followee).name.startsWith("%GRP")){
                newString = newString + "%GRP " + fg.followee + "\n";
            }
        }
        text.setText(newString);
    }
    
    
}
