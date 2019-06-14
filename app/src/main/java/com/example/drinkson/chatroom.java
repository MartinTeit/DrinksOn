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

        List<user> userList;
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

        userList = repository.getAllUsers();
        update(userList);

        someList = repository.remoteGetTable(Repository.USERS);
        for (String s: someList){
            repository.insertUser(JSONConverter.decodeUser(s));
        }
    }

    private void search(){
        List<user> userList;
        List<user> myList = new ArrayList<>();

        userList = repository.searchFollowedGroups(text.getText().toString());
        userList.addAll(repository.searchUser(text.getText().toString()));

        for ( user u: userList) {
            myList.add(u);
        }


        update(myList);
    }

    private void update(List<user> users){
        List<user> usersToShow = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.chats);
        ChatRoomAdapter chatRoomAdapter;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        for(user u: users){
            if(!u.id.equals(currentuser.getCurrentUser())){
                usersToShow.add(u);
            }
        }

        recyclerView.setLayoutManager(layoutManager);
        chatRoomAdapter = new ChatRoomAdapter(usersToShow, this);
        recyclerView.setAdapter(chatRoomAdapter);
    }


}
