package com.example.drinkson;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<String> messages;

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ChatViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.text);
        }
    }

    public ChatAdapter(List<String> text){
        this.messages = text;
    }


    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parnet, int viewType) {

        System.out.println("hej");

        View view = LayoutInflater.from(parnet.getContext())
                .inflate(R.layout.chat_text_row, parnet, false);

        ChatViewHolder viewHolder = new ChatViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder chatViewHolder, int i) {
        String[] m = this.messages.toArray(new String[0]);

        chatViewHolder.textView.setText(m[i]);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

}
