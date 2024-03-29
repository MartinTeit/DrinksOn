package com.example.drinkson;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    //Username
    public String id;

    @ColumnInfo(name = "name")
    //Users name
    public String name;

    @ColumnInfo(name = "stamp")
    //Timestamp
    public long stamp;
}
