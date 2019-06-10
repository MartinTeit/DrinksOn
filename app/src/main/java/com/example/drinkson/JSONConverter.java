package com.example.drinkson;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JSONConverter {




    public static String userEncoder(user u){
        String json;
        String zdt = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(u.stamp),
                ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        json = "{";

        json = json + "\"id\":\"" + u.id + "\"";
        json = json + ",";
        json = json + "\"name\":\"" + u.name + "\"";
        json = json + ",";

        json = json + "\"name\":\"" + zdt + "\"";

        json = json + "}";
        return json;
    }



}
