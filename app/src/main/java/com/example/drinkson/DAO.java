package com.example.drinkson;

import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface DAO {
    @Query("SELECT * FROM user")
    List<user> getUsers();

    @Query("SELECT * FROM user WHERE id LIKE :search AND NOT name LIKE '#%GRP%' ESCAPE '#' LIMIT 100")
    List<user> getSearchUser(String search);


    @Query("SELECT * FROM user, follows WHERE " +
            "follower = :follower AND " +
            "id = followee AND " +
            "id LIKE :search AND " +
            "name LIKE '#%GRP%' ESCAPE '#'")
    List<user> searchFollowedGroups(String search, String follower);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(user user);

    @Query("SELECT * FROM user WHERE id = :searchUser LIMIT 100")
    user findUser(String searchUser);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFollows(follows follows);

    //Person who is beeing followed
    @Query("SELECT follower FROM follows WHERE followee = :followee")
    List<String> findFollowers(String followee);

    //Person who follows
    @Query("SELECT followee FROM follows WHERE follower = :follower")
    List<String> findFollowees(String follower);

    //Person who is beeing followed
    @Query("SELECT * FROM follows WHERE followee = :followee")
    List<follows> getFollowers(String followee);

    //Person who follows
    @Query("SELECT * FROM follows WHERE follower = :follower")
    List<follows> getFollowees(String follower);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insertMessage(messages messages);

    @Query("SELECT * FROM messages WHERE sender = :sender AND receiver = :receiver OR sender = :receiver AND receiver = :sender" )
    List<messages> findConversation(String sender, String receiver);

    @Query("SELECT * FROM messages WHERE receiver = :receiver" )
    List<messages> findGroupChat(String receiver);

    @Query("SELECT * FROM messages WHERE sender = :sender OR receiver = :sender ORDER BY stamp" )
    List<messages> getAllMyMessages(String sender);
}