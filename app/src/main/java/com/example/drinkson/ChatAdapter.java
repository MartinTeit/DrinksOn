package com.example.drinkson;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Messages> messages;

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private TextView senderName;
        public LinearLayout layout;

        public ChatViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.text);
            senderName = view.findViewById(R.id.senderName);
            layout = view.findViewById(R.id.layout);
        }
    }

    public ChatAdapter(List<Messages> messages){
        this.messages = messages;
    }


    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parnet, int viewType) {

        View view = LayoutInflater.from(parnet.getContext())
                .inflate(R.layout.chat_text_row, parnet, false);

        ChatViewHolder viewHolder = new ChatViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder chatViewHolder, int i) {
        Messages m = messages.get(i);
        chatViewHolder.textView.setText(m.body);

        if(m.sender.equals(CurrentUser.getCurrentUser())){
            // Sets the Messages visual as sender
            chatViewHolder.textView.setBackgroundResource(R.drawable.rounded_corner_sender);
            chatViewHolder.layout.setGravity(LinearLayout.TEXT_ALIGNMENT_VIEW_START);
            chatViewHolder.textView.setTextColor(Color.rgb(30,30,30));
            chatViewHolder.layout.setPaddingRelative(128,8,8,8);
            chatViewHolder.senderName.setVisibility(LinearLayout.GONE);
        } else {
            // Sets the Messages visual as receiver
            chatViewHolder.textView.setBackgroundResource(R.drawable.rounded_corner_receiver);
            chatViewHolder.layout.setGravity(LinearLayout.TEXT_ALIGNMENT_VIEW_END);
            chatViewHolder.textView.setTextColor(Color.rgb(30,30,30));
            chatViewHolder.layout.setPadding(8,8,128,8);

            // display the name of the sender
            if(i == 0 || !m.sender.equals(messages.get(i-1).sender)) {
                chatViewHolder.senderName.setVisibility(LinearLayout.VISIBLE);
                chatViewHolder.senderName.setText(m.sender);
                chatViewHolder.senderName.setPadding(16, 2, 0, 2);
                chatViewHolder.senderName.setTextColor(Color.rgb(100, 100, 100));
            } else {
                // if the previous sender was the same don't display the name
                chatViewHolder.senderName.setVisibility(LinearLayout.GONE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

}
