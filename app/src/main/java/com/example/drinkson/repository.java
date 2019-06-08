package com.example.drinkson;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class repository {
    private final String DB_NAME = "Danskere";
    private DAO myDAO;

    public repository(Context context){
        myDAO = Room.databaseBuilder(context, localdatabase.class, DB_NAME).build().getDAO();

    }


    public void insertMessage(messages message) {
        new InsertMessageAsyncTask(myDAO).execute(message);
    }


    public List<messages> getAllMyMessages() {
        try {
            return new getAllMyMessagesAsyncTask(myDAO).execute(currentuser.getCurrentUser()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
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

    //Async task classes
    private static class getAllMyMessagesAsyncTask extends AsyncTask<String, Void, List<messages>> {
        private DAO myDAO;

        private getAllMyMessagesAsyncTask(DAO aDAO){
            this.myDAO = aDAO;
        }

        @Override
        protected List<messages> doInBackground(String... user) {
            return myDAO.getAllMyMessages(user[0]);
        }
    }


}
