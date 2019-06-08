package com.example.drinkson;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class repository {
    private final String DB_NAME = "Danskere";
    private DAO myDAO;

    private LiveData<List<messages>> allMyMessages;
    private LiveData<List<user>> allUsers;

    public repository(Context context){
        myDAO = Room.databaseBuilder(context, localdatabase.class, DB_NAME).build().getDAO();

        //If a user is logged in the application can fetch all it messages
        if(currentuser.getCurrentUser() != null){
            allMyMessages = myDAO.gteAllMyMessages(currentuser.getCurrentUser());
        }

    }

    public void insertMessage(messages message){
        new InsertMessageAsyncTask(myDAO).execute(message);
    }


    //Async task classes
    private static class InsertMessageAsyncTask extends AsyncTask<messages, Void, Void> {
        private DAO myDAO;

        private InsertMessageAsyncTask(DAO aDAO){
            this.myDAO = aDAO;
        }

        @Override
        protected Void doInBackground(messages... messages) {
            myDAO.insertMessage(messages[0]);
            return null;
        }
    }


}
