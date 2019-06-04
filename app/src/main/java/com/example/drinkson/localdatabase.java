package com.example.drinkson;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {user.class}, version = 1, exportSchema = false)
public abstract class localdatabase extends RoomDatabase {
    public abstract DAO getDAO();
}

