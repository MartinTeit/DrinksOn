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

    // Repository constructor builds room data base to access the local data base
    public Repository(Context context){
        myDAO = Room.databaseBuilder(context, localdatabase.class, DB_NAME).build().getDAO();
    }

    //Remote requests

    // Posts "post" in the form of json string to the table "table" of the remote database
    // Returns the response code
    public int remotePost(String table, String post){
        String url = URL + table;

        try {
            return new remotePostAsyncTask(url).execute(post).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Gets the json string of the entry with the specific id from the remote database
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

    // Gets all messages send or received by a user
    public List<messages> remoteGetMessages(String userID){
        String url;
        List<String> myMessagesJson = new ArrayList<>();
        List<messages> myMessages = new ArrayList<>();

        try {
            // get the messages send by the user
            url = URL + MESSAGES + "?sender=eq." + userID;
            myMessagesJson = new remoteGetAsyncTask(url).execute().get();
            // get the received send by the user
            url = URL + MESSAGES + "?receiver=eq." + userID;
            myMessagesJson.addAll(new remoteGetAsyncTask(url).execute().get());

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // translates the messages from json strings to java object's
        for (String json : myMessagesJson) {
            // ignore the message if it's empty
            if (!json.equals("[]") && !json.equals("")) {
                myMessages.add(JSONConverter.decodeMessage(json));
            }
        }

        return myMessages;
    }

    // Gets all follows where the user is the followee
    public List<follows> remoteGetFollowers(String id){
        String url;
        List<String> myFollowersJson = new ArrayList<>();
        List<follows> myFollowers = new ArrayList<>();

        try {
            url = URL + FOLLOWS + "?followee=eq." + id;
            myFollowersJson = new remoteGetAsyncTask(url).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // translates the follows from json strings to java object's
        for (String json : myFollowersJson) {
            // ignore the follows if it's empty
            if (!json.equals("[]") && !json.equals("")) {
                myFollowers.add(JSONConverter.decodeFollows(json));
            }
        }

        return myFollowers;
    }

    // Gets all follows where the user is the follower
    public List<follows> remoteGetFollowees(String id){
        String url;
        List<String> myFollowersJson = new ArrayList<>();
        List<follows> myFollowers = new ArrayList<>();

        try {
            url = URL + FOLLOWS + "?follower=eq." + id;
            myFollowersJson = new remoteGetAsyncTask(url).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // translates the follows from json strings to java object's
        for (String json : myFollowersJson) {
            // ignore the follows if it's empty
            if (!json.equals("[]") && !json.equals("")) {
                myFollowers.add(JSONConverter.decodeFollows(json));
            }
        }

        return myFollowers;
    }

    // Gets the entire table from the remote database
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

    // local request
    public void insertUser(user user) {
        new InsertUserAsyncTask(myDAO).execute(user);
    }

    // gets all users
    public List<user> getUsers() {
        try {
            return new getAllUsersAsyncTask(myDAO).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Gets all users containing "search" in their id
    public List<user> searchUsers(String search) {
        search = "%" + search + "%";
        try {
            return new searchUserAsyncTask(myDAO).execute(search).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Gets all groups followed by the current user and contains the string "search" in their id
    public List<user> searchFollowedGroups(String search) {
        search = "%" + search + "%";
        try {
            return new searchGroupAsyncTask(myDAO).execute(search).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    // returns the user with the id
    public user findUser(String userID) {
        try {
            return new findUserAsyncTask(myDAO).execute(userID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    // insert a message
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

    // gets all messages send by or received by the current user
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

    // gets all messages send to a group
    public List<messages> getGroupChat(String GroupID) {
        try {
            return new getAllMyMessagesAsyncTask(myDAO).execute(GroupID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    // inserts follows
    public void insertFollows(follows follows) {
        new InsertFollowsAsyncTask(myDAO).execute(follows);
    }

    // gets all follows where current user is the followee
    public List<follows> getAllMyFollowers() {
        try {
            return new getAllMyFollowersAsyncTask(myDAO).execute(currentuser.getCurrentUser()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    // gets all follows where current user is the follower
    public List<follows> getAllMyFollowees() {
        try {
            return new getAllMyFolloweesAsyncTask(myDAO).execute(currentuser.getCurrentUser()).get();
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
            return myDAO.getUsers();
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


    private static class searchGroupAsyncTask extends AsyncTask<String, Void, List<user>> {
        private DAO myDAO;

        private searchGroupAsyncTask(DAO aDAO){
            this.myDAO = aDAO;
        }

        @Override
        protected List<user> doInBackground(String... search) {
            return myDAO.searchFollowedGroups(search[0], currentuser.getCurrentUser());
        }
    }


    private static class findUserAsyncTask extends AsyncTask<String, Void, user> {
        private DAO myDAO;

        private findUserAsyncTask(DAO aDAO){
            this.myDAO = aDAO;
        }

        @Override
        protected user doInBackground(String... userID) {
            return myDAO.findUser(userID[0]);
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


    private static class getAllMyFollowersAsyncTask extends AsyncTask<String, Void, List<follows>> {
        private DAO myDAO;

        private getAllMyFollowersAsyncTask(DAO aDAO){
            this.myDAO = aDAO;
        }

        @Override
        protected List<follows> doInBackground(String... user) {
            return myDAO.getFollowers(user[0]);
        }
    }


    private static class getAllMyFolloweesAsyncTask extends AsyncTask<String, Void, List<follows>> {
        private DAO myDAO;

        private getAllMyFolloweesAsyncTask(DAO aDAO){
            this.myDAO = aDAO;
        }

        @Override
        protected List<follows> doInBackground(String... user) {
            return myDAO.getFollowees(user[0]);
        }
    }


    private static class InsertFollowsAsyncTask extends AsyncTask<follows, Void, Void> {
        private DAO myDAO;

        private InsertFollowsAsyncTask(DAO aDAO){
            this.myDAO = aDAO;
        }

        @Override
        protected Void doInBackground(follows... follows) {
            myDAO.insertFollows(follows[0]);

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


    // Async Task to get anything from the remote database
    private static class remoteGetAsyncTask extends AsyncTask<Void, Void, List<String>> {

        // The url from where to retrieve from
        private String url;
        private remoteGetAsyncTask(String url){
            this.url = url;
        }

        @Override
        protected List<String> doInBackground(Void... Void){
            URL obj = null;
            HttpsURLConnection connection = null;
            BufferedReader input;
            String inputLine;
            List<String> returnValue = new ArrayList<>();

            try {
                obj = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                // Opens the connection
                connection = (HttpsURLConnection) obj.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                // Specify that we want to get something from the database
                connection.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            // sets authorization to to get from the database
            connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAxOSJ9.3MGDqJYkivAsiMOXwvoPTD6_LTCWkP3RvI2zpzoB1XE");
            connection.setRequestProperty("Content-Type", "application/json");

            try {
                // Gets the response code to see how the request went
                int responseCode = connection.getResponseCode();
                System.out.println("Response code: " + responseCode);

                // the result from the request
                input = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                // Puts each line from the result into a list of strings
                // each line is in the form of a json
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

    // Async Task to post anything to the remote database
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
            int code = 0;

            try {
                obj = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                // Opens the connection
                connection = (HttpsURLConnection) obj.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                connection.setDoOutput(true);

                // Specify that we want to post something to the database
                connection.setRequestMethod("POST");

                // sets authorization to to get from the database
                connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAxOSJ9.3MGDqJYkivAsiMOXwvoPTD6_LTCWkP3RvI2zpzoB1XE");
                connection.setRequestProperty("Content-Type", "application/json");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            try {
                // Writes to the database
                wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(post[0]);
                wr.flush();
                wr.close();

                code = connection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Posts the response code if the request didn't go ok
            if (code != HttpsURLConnection.HTTP_CREATED) {
                System.out.println("Failed : HTTP error code : " + code);
            }

            return code;
        }
    }

}
