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

        long time = System.currentTimeMillis();
        System.out.println(time);


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

    private static int findEnd(String json, int start){
        int end = start;
            while ( (json.charAt(end) != '\"' || json.charAt(end-1) == '\\') && end <= json.length()){
                end++;
            }
        return end;
    }



}
