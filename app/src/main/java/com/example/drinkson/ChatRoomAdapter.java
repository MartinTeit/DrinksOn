package com.example.drinkson;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;


public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {

    public List<user> chats;

    private static Context context;

    public static class ChatRoomViewHolder extends RecyclerView.ViewHolder {

        public Button chatButton;
        public ChatRoomViewHolder(View view) {
            super(view);

            chatButton = view.findViewById(R.id.chatButton);

            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chat.receiver = chatButton.getContentDescription().toString();
                    openChat();
                }
            });

        }

    }

    public ChatRoomAdapter(List<user> chats, Context context){
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
        user u = this.chats.get(i);
        chatViewHolder.chatButton.setContentDescription(u.id);
        if(u.name.startsWith("%GRP")){
            chatViewHolder.chatButton.setText("Group: " + u.id);
        } else {
            chatViewHolder.chatButton.setText(u.id);
        }

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