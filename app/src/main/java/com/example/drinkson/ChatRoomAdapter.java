package com.example.drinkson;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;


public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {

    public List<String> chats;

    private static Context context;

    public static class ChatRoomViewHolder extends RecyclerView.ViewHolder {

        public Button ChatButton;
        public ChatRoomViewHolder(View view) {
            super(view);
            ChatButton = view.findViewById(R.id.chatButton);

            ChatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chat.receiver = ChatButton.getText().toString();
                    openChat();
                }
            });

        }

    }

    public ChatRoomAdapter(List<String> chats, Context context){
        this.chats = chats;
        this.context = context;
    }


    @Override
    public ChatRoomViewHolder onCreateViewHolder(ViewGroup parnet, int viewType) {

        View view = LayoutInflater.from(parnet.getContext())
                .inflate(R.layout.chat_room_row, parnet, false);

        ChatRoomViewHolder viewHolder = new ChatRoomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatRoomViewHolder chatViewHolder, int i) {
        String[] m = this.chats.toArray(new String[0]);

        chatViewHolder.ChatButton.setText(m[i]);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static void openChat() {
        Intent intent = new Intent(context, chat.class);
        context.startActivity(intent);
    }


}