package com.example.drinkson;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class Repository {

    public static final String URL = "https://caracal.imada.sdu.dk/app2019/";
    public static final String USERS = "users";
    public static final String MESSAGES = "messages";
    public static final String FOLLOWS = "follows";
    private static final String AUTORIZATION =
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                    "eyJyb2xlIjoiYXBwMjAxOSJ9.3MGDqJYkivAsiMOXwvoPTD6_LTCWkP3RvI2zpzoB1XE";

    private final String DB_NAME = "Danskere";
    private DAO myDAO;

    public Repository(Context context){
        myDAO = Room.databaseBuilder(context, localdatabase.class, DB_NAME).build().getDAO();

    }

    //Remote requests
    public int remotePost(String table, String post){
        String url = URL + table;

        try {
            return new remotePostAsyncTask(url).execute(post).get().intValue();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public String remoteGetByID(String table, String id){
        String url = URL + table + "?id=eq." + id;

        try {
            return new remoteGetAsyncTask(url).execute().get().get(0);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<messages> remoteGetMessages(String id){
        String url;
        List<String> myMessagesJson = new ArrayList<>();
        List<messages> myMessages = new ArrayList<>();

        try {
            url = URL + MESSAGES + "?sender=eq." + id;
            myMessagesJson = new remoteGetAsyncTask(url).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            url = URL + MESSAGES + "?receiver=eq." + id;
            myMessagesJson.addAll(new remoteGetAsyncTask(url).execute().get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (String json : myMessagesJson) {
            System.out.println("hey Hey");
            System.out.println(json);

            if (!json.equals("[]") && !json.equals("")) {
                myMessages.add(JSONConverter.decodeMessage(json));
            }
        }

        return myMessages;
    }

    public List<String> remoteGetTable(String table){
        String url = URL + table;

        try {
            return new remoteGetAsyncTask(url).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    //local request
    public void insertUser(user user) {
        new InsertUserAsyncTask(myDAO).execute(user);
    }

    public long insertMessage(messages message) {
        try {
            return new InsertMessageAsyncTask(myDAO).execute(message).get().longValue();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteMessage(messages message) {
        new DeleteMessageAsyncTask(myDAO).execute(message);
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

    public List<user> getAllUsers() {
        try {
            return new getAllUsersAsyncTask(myDAO).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<user> searchUser(String search) {
        try {
            return new searchUserAsyncTask(myDAO).execute(search).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Async task classes
    private static class InsertUserAsyncTask extends AsyncTask<user, Void, Void> {
        private DAO myDAO;

        private InsertUserAsyncTask(DAO aDAO){
            this.myDAO = aDAO;
        }

        @Override
        protected Void doInBackground(user... user) {
            System.out.println("id: " + user[0].id);
            System.out.println("name: " + user[0].name);
            System.out.println("stamp: " + user[0].stamp);
            myDAO.insertUser(user[0]);
            return null;
        }
    }


    private static class getAllUsersAsyncTask extends AsyncTask<Void, Void, List<user>> {
        private DAO myDAO;

        private getAllUsersAsyncTask(DAO aDAO){
            this.myDAO = aDAO;
        }

        @Override
        protected List<user> doInBackground(Void... Void) {
            return myDAO.getAll();
        }
    }


    private static class searchUserAsyncTask extends AsyncTask<String, Void, List<user>> {
        private DAO myDAO;

        private searchUserAsyncTask(DAO aDAO){
            this.myDAO = aDAO;
        }

        @Override
        protected List<user> doInBackground(String... search) {
            return myDAO.getSearchUser(search[0]);
        }
    }


    private static class InsertMessageAsyncTask extends AsyncTask<messages, Void, Long> {
        private DAO myDAO;

        private InsertMessageAsyncTask(DAO aDAO){
            this.myDAO = aDAO;
        }

        @Override
        protected Long doInBackground(messages... messages) {
            return myDAO.insertMessage(messages[0]);
        }
    }

    private static class DeleteMessageAsyncTask extends AsyncTask<messages, Void, Void> {
        private DAO myDAO;

        private DeleteMessageAsyncTask(DAO aDAO){
            this.myDAO = aDAO;
        }

        @Override
        protected Void doInBackground(messages... messages) {
            myDAO.deleteMessage(messages[0]);

            return null;
        }
    }


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


    private static class remoteGetAsyncTask extends AsyncTask<Void, Void, List<String>> {

        private String url;
        private remoteGetAsyncTask(String url){
            this.url = url;
        }

        @Override
        protected List<String> doInBackground(Void... Void){
            URL obj = null;
            HttpsURLConnection connection = null;
            BufferedReader input;
            String inputLine = null;
            List<String> returnValue = new ArrayList<>();

            try {
                obj = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                connection = (HttpsURLConnection) obj.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                connection.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAxOSJ9.3MGDqJYkivAsiMOXwvoPTD6_LTCWkP3RvI2zpzoB1XE");
            connection.setRequestProperty("Content-Type", "application/json");

            try {
                int responseCode = connection.getResponseCode();
                System.out.println(responseCode);

                input = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                while ( (inputLine = input.readLine()) != null ){
                    returnValue.add(inputLine);
                }

                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return returnValue;
        }
    }


    private static class remotePostAsyncTask extends AsyncTask<String, Void, Integer> {

        private String url;
        private remotePostAsyncTask(String url){
            this.url = url;
        }

        @Override
        protected Integer doInBackground(String... post){

            URL obj = null;
            HttpsURLConnection connection = null;
            DataOutputStream wr;
            Integer code = 0;

            try {
                obj = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                connection = (HttpsURLConnection) obj.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAxOSJ9.3MGDqJYkivAsiMOXwvoPTD6_LTCWkP3RvI2zpzoB1XE");
                connection.setRequestProperty("Content-Type", "application/json");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            try {
                wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(post[0]);
                wr.flush();
                wr.close();

                code = connection.getResponseCode();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (code != HttpsURLConnection.HTTP_CREATED) {
                System.out.println("Failed : HTTP error code : " + code);
            }

            return code;
        }
    }


}
