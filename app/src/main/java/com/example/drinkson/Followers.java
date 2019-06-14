package com.example.drinkson;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Followers extends AppCompatActivity {

    private Button   follow;
    private TextView text;
    private Repository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        repository = new Repository(this);

        final EditText followerUsername = findViewById(R.id.followerUsername);
        final localdatabase database    = Room.databaseBuilder(getApplicationContext(),
                localdatabase.class, "Danskere").build();
        final DAO dao = database.getDAO();

        follow = findViewById(R.id.button);
        text   = findViewById(R.id.name);

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final follows newFollows = new follows();

                newFollows.follower = currentuser.getCurrentUser();
                newFollows.followee = followerUsername.getText().toString();
                newFollows.stamp    = System.currentTimeMillis();

                followUser(newFollows);
                updateFollowers();
            }
        });

        List<follows> followers = repository.remoteGetFollowers(currentuser.getCurrentUser());

        for(follows f : followers){
            repository.insertFollows(f);
        }

        updateFollowers();
    }


    public void followUser(follows newFollows){
        int responseCode;

        responseCode = repository.remotePost(Repository.FOLLOWS,JSONConverter.encodeFollows(newFollows));

        if (responseCode != HttpsURLConnection.HTTP_CONFLICT) {
            repository.insertFollows(newFollows);
        }
    }

    public void updateFollowers(){
        String newString="";
        for (follows f : repository.getAllMyFollowers()){
            newString = newString + f.follower + "\n";
        }
        text.setText(newString);
    }

}
