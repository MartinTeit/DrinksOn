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

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertFollows(follows follows);

    @Query("SELECT follower FROM follows WHERE followee = :followee")
    String findFollowers(String followee);

    @Query("SELECT followee FROM follows WHERE follower = :follower")
    String findFollowees(String follower);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertMessage(messages messages);

    @Query("SELECT body FROM messages WHERE sender = :sender AND receiver = :receiver")
    String findConversation(String sender, String receiver);
}