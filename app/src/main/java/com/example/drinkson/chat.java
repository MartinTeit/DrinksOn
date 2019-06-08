package com.example.drinkson;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
public class chat extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button sendButton;
    private EditText text;
    private List<messages> myMessages;

    private repository repository;


    public static String receiver;
    private List<String> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        updateMessages();

        text = findViewById(R.id.editText);
        sendButton = findViewById(R.id.send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(text.getText().toString());
                text.setText("");
            }
        });

    }


    private  void updateMessages(){
        repository = new repository(this);

        myMessages = repository.getAllMyMessages();

        messages = new ArrayList<>();

        for(messages m: myMessages){

            if(m.receiver.equals(receiver) || m.sender.equals(receiver)){
                this.messages.add(m.body);
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.messages);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        chatAdapter = new ChatAdapter(myMessages);
        recyclerView.setAdapter(chatAdapter);
    }


    private void sendMessage(String text){

        final messages newMessage = new messages();
        newMessage.sender = currentuser.getCurrentUser();
        newMessage.receiver = receiver;
        newMessage.body = text;
        newMessage.stamp = System.currentTimeMillis();

        repository.insertMessage(newMessage);

        updateMessages();

    }
}