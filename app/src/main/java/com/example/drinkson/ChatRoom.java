package com.example.drinkson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom extends AppCompatActivity {
    private Repository repository;
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        List<User> userList;
        List<String> someList;
        Button searchButton = findViewById(R.id.searchButton);

        text = findViewById(R.id.searchUser);
        repository = new Repository(this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        // Gets users and Follows from the remote
        // Follows is only used to determine which groups to show
        someList = repository.remoteGetTable(Repository.USERS);
        for (String s: someList){
            repository.insertUser(JSONConverter.decodeUser(s));
        }
        List<Follows> followees = repository.remoteGetFollowees(CurrentUser.getCurrentUser());
        for(Follows f : followees){
            repository.insertFollows(f);
        }

        // Starting the activity with an empty string search meaning
        // it finds every thing, with a limit of returned entries sat by the query in DAO
        userList = repository.searchFollowedGroups("");
        userList.addAll(repository.searchUsers(""));
        update(userList);

    }

    // Finds users and followed groups containing the string "search"
    private void search(){
        List<User> userList;

        userList = repository.searchFollowedGroups(text.getText().toString());
        userList.addAll(repository.searchUsers(text.getText().toString()));

        update(userList);
    }

    // Updates rhe recyclerView in chatRoom
    private void update(List<User> users){
        List<User> usersToShow = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.chats);
        ChatRoomAdapter chatRoomAdapter;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        for(User u: users){
            if(!u.id.equals(CurrentUser.getCurrentUser())){
                usersToShow.add(u);
            }
        }

        recyclerView.setLayoutManager(layoutManager);
        chatRoomAdapter = new ChatRoomAdapter(usersToShow, this);
        recyclerView.setAdapter(chatRoomAdapter);
    }


}
