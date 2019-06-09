package com.example.drinkson;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//hej
@Entity
public class user {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    //Username
    public String id;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "name")
    //Users name
    public String name;

    @ColumnInfo(name = "stamp")
    //Timestamp
    public long stamp;
}
