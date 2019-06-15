package com.example.drinkson;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Messages {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "sender")
    public String sender;

    @ColumnInfo(name = "receiver")
    public String receiver;

    @ColumnInfo(name = "body")
    public String body;

    @ColumnInfo(name = "stamp")
    //Timestamp
    public long stamp;
}