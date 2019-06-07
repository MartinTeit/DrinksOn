package com.example.drinkson;

import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface DAO {
    @Query("SELECT * FROM user")
    List<user> getAll();

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertUser(user user);

    @Query("SELECT * FROM user WHERE id = :searchUser")
    user findUser(String searchUser);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //Inserts followers to given list
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertFollows(follows follows);

    //Person who is beeing followed
    @Query("SELECT follower FROM follows WHERE followee = :followee")
    List<String> findFollowers(String followee);

    //Person who follows
    @Query("SELECT followee FROM follows WHERE follower = :follower")
    List<String> findFollowees(String follower);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertMessage(messages messages);

    @Query("SELECT body FROM messages WHERE sender = :sender AND receiver = :receiver")
    List<String> findConversation(String sender, String receiver);
}