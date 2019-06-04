package com.example.drinkson;

import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface DAO {
    @Query("SELECT * FROM user")
    List<user> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(user user);
}