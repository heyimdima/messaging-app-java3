package com.gcu.messagingapp.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository {
    @Modifying
    @Query("INSERT INTO friends (user_id, friend_id) VALUES (:userId, :friendId)")
    void addFriend(Long userId, Long friendId);
}