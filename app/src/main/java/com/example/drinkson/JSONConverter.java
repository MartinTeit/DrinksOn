package com.example.drinkson;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JSONConverter {




    public static String encodeUser(user u){
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

    public static user decodeUser(String json){
        int idStart;
        int nameStart;
        int stampStart;
        String stamp;

        user u = new user();

        idStart = json.indexOf("\"id\":\"") + 6;
        u.id = json.substring(idStart,findEnd(json,idStart));

        nameStart = json.indexOf("\"name\":\"") + 8;
        u.name = json.substring(nameStart,findEnd(json,nameStart));

        stampStart = json.indexOf("\"stamp\":\"") + 9;
        stamp = json.substring(stampStart,findEnd(json,stampStart));
        u.stamp = ZonedDateTime.parse(stamp).toInstant().toEpochMilli();

        return u;
    }

    public static String encodeMessages(messages m){
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

    public static messages decodeMessage(String json){
        int indexID;
        int indexSender;
        int indexReceiver;
        int indexBody;
        int indexStamp;
        String stamp;

        messages m = new messages();

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
