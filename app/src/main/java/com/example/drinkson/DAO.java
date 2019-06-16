package com.example.drinkson;

import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface DAO {
    @Query("SELECT * FROM User")
    List<User> getUsers();

    @Query("SELECT * FROM User WHERE id LIKE :search AND NOT name LIKE '#%GRP%' ESCAPE '#' LIMIT 100")
    List<User> getSearchUser(String search);


    @Query("SELECT User.id, User.name, User.stamp FROM User, Follows WHERE " +
            "follower = :follower AND " +
            "id = followee AND " +
            "id LIKE :search AND " +
            "name LIKE '#%GRP%' ESCAPE '#'")
    List<User> searchFollowedGroups(String search, String follower);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("SELECT * FROM User WHERE id = :searchUser LIMIT 100")
    User findUser(String searchUser);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFollows(Follows follows);

    //Person who is being followed
    @Query("SELECT follower FROM Follows WHERE followee = :followee")
    List<String> findFollowers(String followee);

    //Person who Follows
    @Query("SELECT followee FROM Follows WHERE follower = :follower")
    List<String> findFollowees(String follower);

    //Person who is beeing followed
    @Query("SELECT * FROM Follows WHERE followee = :followee")
    List<Follows> getFollowers(String followee);

    //Person who Follows
    @Query("SELECT * FROM Follows WHERE follower = :follower")
    List<Follows> getFollowees(String follower);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insertMessage(Messages messages);

    @Query("SELECT * FROM Messages WHERE sender = :sender AND receiver = :receiver OR sender = :receiver AND receiver = :sender" )
    List<Messages> findConversation(String sender, String receiver);

    @Query("SELECT * FROM Messages WHERE receiver = :receiver" )
    List<Messages> findGroupChat(String receiver);

    @Query("SELECT * FROM Messages WHERE sender = :sender OR receiver = :sender ORDER BY stamp" )
    List<Messages> getAllMyMessages(String sender);
}