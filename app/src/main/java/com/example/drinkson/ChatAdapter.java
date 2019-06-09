package com.example.drinkson;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<messages> messages;

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public LinearLayout layout;

        public ChatViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.text);
            layout = view.findViewById(R.id.layout);
        }
    }

    public ChatAdapter(List<messages> messages){
        this.messages = messages;
    }


    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parnet, int viewType) {

        View view = LayoutInflater.from(parnet.getContext())
                .inflate(R.layout.chat_text_row, parnet, false);

        ChatViewHolder viewHolder = new ChatViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder chatViewHolder, int i) {
        messages m = messages.get(i);

        chatViewHolder.textView.setText(m.body);

        if(m.sender.equals(currentuser.getCurrentUser())){
            chatViewHolder.textView.setBackgroundResource(R.drawable.rounded_corner_sender);
            chatViewHolder.layout.setGravity(LinearLayout.TEXT_ALIGNMENT_VIEW_START);
            chatViewHolder.textView.setTextColor(Color.rgb(30,30,30));

        } else {
            chatViewHolder.textView.setBackgroundResource(R.drawable.rounded_corner_receiver);
            chatViewHolder.layout.setGravity(LinearLayout.TEXT_ALIGNMENT_VIEW_END);
            chatViewHolder.textView.setTextColor(Color.rgb(30,30,30));
        }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

}
