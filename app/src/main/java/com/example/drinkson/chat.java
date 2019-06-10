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

public class chat extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button sendButton;
    private EditText text;
    private List<messages> myMessages;

    private Repository repository;


    public static String receiver;
    private List<String> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        repository = new Repository(this);

        List<messages> someMessages = repository.remoteGetMessages(currentuser.getCurrentUser());

        for(messages m : someMessages){
            repository.insertMessage(m);
        }

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

        myMessages = repository.getAllMyMessages();
        List<messages> messagesInThisConversation = new ArrayList<>();
        messages = new ArrayList<>();

        for(messages m: myMessages){
            System.out.println(m.id);
            if(m.receiver.equals(receiver) || m.sender.equals(receiver)){
                messagesInThisConversation.add(m);
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.messages);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        chatAdapter = new ChatAdapter(messagesInThisConversation);
        recyclerView.setAdapter(chatAdapter);
    }


    private void sendMessage(String text){

        if(!text.equals("")){
            long id;

            final messages newMessage = new messages();
            newMessage.sender = currentuser.getCurrentUser();
            newMessage.receiver = receiver;
            newMessage.body = text;
            newMessage.stamp = System.currentTimeMillis();

            id = repository.insertMessage(newMessage);
            newMessage.id = (int) id;
            repository.remotePost(Repository.MESSAGES, JSONConverter.encodeMessages(newMessage));

            updateMessages();
        }

    }
}