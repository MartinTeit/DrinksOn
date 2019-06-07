package com.example.drinkson;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(primaryKeys = {"follower","followee"})
public class follows {
    @NonNull
    @ColumnInfo(name = "follower")
    //Username
    public String follower;


    @NonNull
    @ColumnInfo(name = "followee")
    public String followee;

    @ColumnInfo(name = "stamp")
    //Timestamp
    public long stamp;


}
