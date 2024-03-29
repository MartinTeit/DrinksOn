package com.example.drinkson;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JSONConverter {

    public static String encodeFollows(Follows f){
        String json;
        String zdt = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(f.stamp),
                ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        json = "{";

        json = json + "\"follower\":\"" + f.follower + "\"";
        json = json + ",";
        json = json + "\"followee\":\"" + f.followee + "\"";
        json = json + ",";

        json = json + "\"stamp\":\"" + zdt + "\"";

        json = json + "}";

        return json;
    }

    public static Follows decodeFollows(String json){
        int followerStart;
        int followeeStart;
        int stampStart;
        String stamp;

        Follows f = new Follows();

        followerStart = json.indexOf("\"follower\":\"") + 12;
        f.follower = json.substring(followerStart,findEnd(json,followerStart));

        followeeStart = json.indexOf("\"followee\":\"") + 12;
        f.followee = json.substring(followeeStart,findEnd(json,followeeStart));

        stampStart = json.indexOf("\"stamp\":\"") + 9;
        stamp = json.substring(stampStart,findEnd(json,stampStart));
        f.stamp = ZonedDateTime.parse(stamp).toInstant().toEpochMilli();

        return f;
    }

    public static String encodeUser(User u){
        String json;
        String zdt = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(u.stamp),
                ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        json = "{";

        json = json + "\"id\":\"" + u.id + "\"";
        json = json + ",";
        json = json + "\"name\":\"" + u.name + "\"";
        json = json + ",";

        json = json + "\"stamp\":\"" + zdt + "\"";

        json = json + "}";

        System.out.println(json);

        return json;
    }

    public static User decodeUser(String json){
        int idStart;
        int nameStart;
        int stampStart;
        String stamp;

        User u = new User();

        idStart = json.indexOf("\"id\":\"") + 6;
        u.id = json.substring(idStart,findEnd(json,idStart));

        nameStart = json.indexOf("\"name\":\"") + 8;
        u.name = json.substring(nameStart,findEnd(json,nameStart));

        stampStart = json.indexOf("\"stamp\":\"") + 9;
        stamp = json.substring(stampStart,findEnd(json,stampStart));
        u.stamp = ZonedDateTime.parse(stamp).toInstant().toEpochMilli();

        return u;
    }

    public static String encodeMessages(Messages m){
        String json;
        String zdt = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(m.stamp),
                ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        json = "{";

        json = json + "\"id\":\"" + m.id + "\"";
        json = json + ",";

        json = json + "\"sender\":\"" + m.sender + "\"";
        json = json + ",";

        json = json + "\"receiver\":\"" + m.receiver + "\"";
        json = json + ",";

        json = json + "\"body\":\"" + m.body + "\"";
        json = json + ",";

        json = json + "\"stamp\":\"" + zdt + "\"";

        json = json + "}";

        System.out.println(json);

        return json;
    }

    public static Messages decodeMessage(String json){
        int indexID;
        int indexSender;
        int indexReceiver;
        int indexBody;
        int indexStamp;
        String stamp;

        Messages m = new Messages();

        indexID = json.indexOf("\"id\":") + 5;
        m.id = Integer.parseInt(json.substring(indexID,findEndOfNumber(json,indexID)));

        indexSender = json.indexOf("\"sender\":\"") + 10;
        m.sender = json.substring(indexSender,findEnd(json,indexSender));

        indexReceiver = json.indexOf("\"receiver\":\"") + 12;
        m.receiver = json.substring(indexReceiver,findEnd(json,indexReceiver));

        indexBody = json.indexOf("\"body\":\"") + 8;
        m.body = json.substring(indexBody,findEnd(json,indexBody));

        indexStamp = json.indexOf("\"stamp\":\"") + 9;
        stamp = json.substring(indexStamp,findEnd(json,indexStamp));
        m.stamp = ZonedDateTime.parse(stamp).toInstant().toEpochMilli();

        return m;
    }

    private static int findEnd(String json, int start){

        int end = start;
            while ( (json.charAt(end) != '\"' || json.charAt(end-1) == '\\') && end <= json.length()){
                end++;
            }
        return end;
    }

    private static int findEndOfNumber(String json, int start){

        int end = start;
        while ( json.charAt(end) != ',' && end <= json.length()){
            end++;
        }
        return end;
    }
}
