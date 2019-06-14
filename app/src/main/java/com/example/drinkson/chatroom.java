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

public class chatroom extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatRoomAdapter chatRoomAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Repository repository;
    private Button searchButton;
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        searchButton = findViewById(R.id.searchButton);
        text = findViewById(R.id.searchUser);
        recyclerView = (RecyclerView) findViewById(R.id.chats);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        repository = new Repository(this);

        List<user> myList = new ArrayList<>();
        List<user> userList;
        List<String> someList;

        userList = repository.getAllUsers();

        for (user users: userList) {
            if( !users.id.equals(currentuser.getCurrentUser()) ){
                myList.add(users);
            }
        }

        update(myList);

        someList = repository.remoteGetTable(Repository.USERS);

        for (String s: someList){
            repository.insertUser(JSONConverter.decodeUser(s));
        }
    }

    private void search(){
        List<user> userList;
        List<user> myList = new ArrayList<>();

        userList = repository.searchUser("%" + text.getText().toString() + "%");

        for ( user u:userList) {
            myList.add(u);
        }


        update(myList);
    }

    private void update(List<user> users){
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        chatRoomAdapter = new ChatRoomAdapter(users, this);
        recyclerView.setAdapter(chatRoomAdapter);
    }


}
