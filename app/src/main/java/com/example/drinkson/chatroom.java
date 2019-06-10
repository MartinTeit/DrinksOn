package com.example.drinkson;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.support.v4.content.ContextCompat.startActivity;

public class chatroom extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatRoomAdapter chatRoomAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        repository = new Repository(this);

        final localdatabase database = Room.databaseBuilder(
                getApplicationContext(),
                localdatabase.class,
                "Danskere"
        ).build();

        List<String> myList = new ArrayList<>();

        List<user> userList;

        List<String> someList;
        someList = repository.remoteGetTable(Repository.USERS);

        for (String s: someList){
            repository.insertUser(JSONConverter.decodeUser(s));
        }

        userList = repository.getAllUsers();

        for (user users: userList) {
            if( !users.id.equals(currentuser.getCurrentUser()) ){
                myList.add(users.id);
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.chats);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        chatRoomAdapter = new ChatRoomAdapter(myList, this);
        recyclerView.setAdapter(chatRoomAdapter);


    }

}
