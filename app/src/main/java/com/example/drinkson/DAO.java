package com.example.drinkson;

import android.arch.lifecycle.LiveData;
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
    void insertFollows(follows follows);

    //Person who is beeing followed
    @Query("SELECT follower FROM follows WHERE followee = :followee")
    List<String> findFollowers(String followee);

    //Person who follows
    @Query("SELECT followee FROM follows WHERE follower = :follower")
    List<String> findFollowees(String follower);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMessage(messages messages);

    @Query("SELECT body FROM messages WHERE sender = :sender AND receiver = :receiver OR sender = :receiver AND receiver = :sender" )
    List<String> findConversation(String sender, String receiver);

    @Query("SELECT * FROM messages WHERE sender = :sender OR receiver = :sender" )
    LiveData<List<messages>> gteAllMyMessages(String sender);
}