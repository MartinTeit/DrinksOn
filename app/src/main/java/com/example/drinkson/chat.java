package com.example.drinkson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class chat extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private String receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("hej");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = (RecyclerView) findViewById(R.id.messages);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String> myList = new ArrayList<String>();

        myList.add("one");
        myList.add("two");
        myList.add("three");



        chatAdapter = new ChatAdapter(myList);
        recyclerView.setAdapter(chatAdapter);

    }
}
