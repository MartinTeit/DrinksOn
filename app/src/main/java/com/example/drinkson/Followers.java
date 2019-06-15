package com.example.drinkson;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Followers extends AppCompatActivity {

    private TextView text;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        repository = new Repository(this);

        final EditText followerUsername = findViewById(R.id.followerUsername);
        Button  followButton;

        followButton = findViewById(R.id.button);
        text   = findViewById(R.id.name);

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Follows newFollows = new Follows();

                newFollows.follower = CurrentUser.getCurrentUser();
                newFollows.followee = followerUsername.getText().toString();
                newFollows.stamp    = System.currentTimeMillis();

                followUser(newFollows);
                updateFollowers();
            }
        });

        List<Follows> followers = repository.remoteGetFollowers(CurrentUser.getCurrentUser());

        for(Follows f : followers){
            repository.insertFollows(f);
        }

        updateFollowers();
    }

    public void followUser(Follows newFollows){
        int responseCode;

        responseCode = repository.remotePost(Repository.FOLLOWS,JSONConverter.encodeFollows(newFollows));

        if (responseCode != HttpsURLConnection.HTTP_CONFLICT) {
            repository.insertFollows(newFollows);
        }
    }

    public void updateFollowers(){
        String newString="";
        for (Follows f : repository.getAllMyFollowers()){
            newString = newString + f.follower + "\n";
        }
        text.setText(newString);
    }

}
