package com.example.drinkson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class Chat extends AppCompatActivity {

    private EditText text;
    private Repository repository;
    public static String receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        repository = new Repository(this);
        List<Messages> someMessages;
        Button sendButton;

        // gets message from the remote database
        if(repository.findUser(receiver).name.startsWith("%GRP")){
            someMessages = repository.remoteGetMessages(receiver);
        } else {
            someMessages = repository.remoteGetMessages(CurrentUser.getCurrentUser());
        }

        // Input the Messages into the local database to update for new Messages
        for(Messages m : someMessages){
            repository.insertMessage(m);
        }

        updateMessages();

        text = findViewById(R.id.editText);
        sendButton = findViewById(R.id.send);

        // Listener for when sendButton is pressed
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(text.getText().toString());
                text.setText("");
            }
        });
    }

    // Updates the RecyclerView containing the Messages
    private  void updateMessages(){
        RecyclerView recyclerView;
        ChatAdapter chatAdapter;
        LinearLayoutManager layoutManager;

        List<Messages> myMessages;

        List<Messages> messagesInThisConversation = new ArrayList<>();

        // If this is a group Chat get all the group Messages
        // else get all the Messages between current User an the other User
        if(repository.findUser(receiver).name.startsWith("%GRP")){
            myMessages = repository.getGroupChat(receiver);
            for(Messages m: myMessages){
                if(m.sender != null && m.receiver.equals(receiver)){
                    messagesInThisConversation.add(m);
                }
            }
        } else {
            myMessages = repository.getAllMyMessages();
            for(Messages m: myMessages){
                if(m.sender != null && (m.receiver.equals(receiver) || m.sender.equals(receiver) )){
                    messagesInThisConversation.add(m);
                }
            }
        }

        recyclerView = findViewById(R.id.messages);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        chatAdapter = new ChatAdapter(messagesInThisConversation);
        recyclerView.setAdapter(chatAdapter);
    }


    private void sendMessage(String text){

        // don't send the message if it is empty
        if(!text.equals("")){
            long id;
            int responseCode;
            int maxIteration = 10;
            int iteration = 0;
            Messages newMessage;

            // random number generator to gat a random id for the message
            Random randInt = new Random();

            // creates a new message
            newMessage = new Messages();
            newMessage.sender = CurrentUser.getCurrentUser();
            newMessage.receiver = receiver;
            newMessage.body = text;
            newMessage.stamp = System.currentTimeMillis();

            do {
                // Generate a random id for the message
                id = randInt.nextInt();
                newMessage.id = (int) id;
                responseCode = repository.remotePost(Repository.MESSAGES, JSONConverter.encodeMessages(newMessage));
                iteration++;

                // If there was a conflict on the remote database try again with another id
                // while max iteration isn't exceeded
            } while (responseCode == HttpsURLConnection.HTTP_CONFLICT && iteration<maxIteration);

            if(iteration >= maxIteration && responseCode == HttpsURLConnection.HTTP_CONFLICT) {
                // If max iteration exceeded write it in console, and don't post the message
                System.out.println("max iteration exceeded");
            } else if (responseCode == HttpsURLConnection.HTTP_CREATED ) {
                // If nothing went wrong insert the message in the local database
                repository.insertMessage(newMessage);

                // Update the view
                updateMessages();
            } else {
                System.out.println(responseCode);
            }


        }

    }
}