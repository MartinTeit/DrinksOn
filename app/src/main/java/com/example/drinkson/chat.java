package com.example.drinkson;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class chat extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button sendButton;
    private EditText text;
    private localdatabase database;


    public static String receiver;
    private List<String> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

         database = Room.databaseBuilder(
                getApplicationContext(),
                localdatabase.class,
                "Danskere"
        ).build();

        try {
            this.messages = new AsyncTask<Void, Void, List<String>>() {

                @Override
                protected List<String> doInBackground(Void... voids) {
                    return database.getDAO().findConversation(currentuser.getCurrentUser(), receiver);
                }
            }.execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.messages);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        chatAdapter = new ChatAdapter(messages);
        recyclerView.setAdapter(chatAdapter);

        sendButton = findViewById(R.id.send);
        text = findViewById(R.id.editText);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(text.getText().toString());
                text.setText("");
            }
        });

    }


    private void sendMessage(String text){

        System.out.println(text);

        final messages newMessage = new messages();
        newMessage.sender = currentuser.getCurrentUser();
        newMessage.receiver = receiver;
        newMessage.body = text;
        newMessage.stamp = System.currentTimeMillis();
        try {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    database.getDAO().insertMessage(newMessage);
                    return null;
                }
            }.execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
