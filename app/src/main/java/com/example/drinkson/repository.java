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
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class repository {

    public static final String URL = "https://caracal.imada.sdu.dk/app2019/";
    public static final String USERS = "users";
    public static final String MESSAGES = "messages";
    public static final String FOLLOWS = "follows";
    private static final String AUTORIZATION =
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAxOSJ9.3MGDqJYkivAsiMOXwvoPTD6_LTCWkP3RvI2zpzoB1XE";

    private final String DB_NAME = "Danskere";
    private DAO myDAO;

    public repository(Context context){
        myDAO = Room.databaseBuilder(context, localdatabase.class, DB_NAME).build().getDAO();

    }


    public void remotePost(String table, String post){
        String url = URL + table;

        new remotePostAsyncTask(url).execute(post);

    }



    public String remoteGet(String table, String id){
        String url = URL + table + "?id=eq." + id;

        try {
            return new remoteGetAsyncTask(url).execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;

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


    private static class remoteGetAsyncTask extends AsyncTask<String, Void, String> {

        private String url;
        private remoteGetAsyncTask(String url){
            this.url = url;
        }

        @Override
        protected String doInBackground(String... url){
            System.out.println("1");
            URL obj = null;
            try {
                obj = new URL(url[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            System.out.println("2");
            HttpsURLConnection connection = null;
            try {
                connection = (HttpsURLConnection) obj.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("connection: " + connection);
            try {
                connection.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAxOSJ9.3MGDqJYkivAsiMOXwvoPTD6_LTCWkP3RvI2zpzoB1XE");
            connection.setRequestProperty("Content-Type", "application/json");

            System.out.println(url[0]);
            System.out.println("4");
            try {
                int responseCode = connection.getResponseCode();
                System.out.println(responseCode);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("5");
            BufferedReader input = null;
            try {
                input = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String inputLine = null;
            System.out.println("6");

            try {
                inputLine = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("7");

            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return inputLine;
        }
    }


    private static class remotePostAsyncTask extends AsyncTask<String, Void, Void> {

        private String url;
        private remotePostAsyncTask(String url){
            this.url = url;
        }

        @Override
        protected Void doInBackground(String... post){

            System.out.println(post[0]);
            URL obj = null;
            try {
                obj = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpsURLConnection connection = null;
            try {
                connection = (HttpsURLConnection) obj.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("connection: " + connection);
            try {
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAxOSJ9.3MGDqJYkivAsiMOXwvoPTD6_LTCWkP3RvI2zpzoB1XE");
                connection.setRequestProperty("Content-Type", "application/json");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            DataOutputStream wr = null;
            try {
                wr = new DataOutputStream(connection.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                wr.writeBytes(post[0]);
                wr.flush();
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int code = 0;

            try {
                code = connection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (code != HttpsURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + code);
            }

            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(
                        (connection.getInputStream())));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String output = null;
            System.out.println("Output from Server .... \n");
            while (true) {
                try {
                    if (!((output = br.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(output);
            }



            return null;


        }
    }

}
