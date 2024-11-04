package com.gcu.messagingapp.repository;

import com.gcu.messagingapp.model.Message;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    @Query("SELECT * FROM messages WHERE (sender_id = :senderId AND receiver_id = :receiverId) " +
            "OR (sender_id = :receiverId AND receiver_id = :senderId) " +
            "ORDER BY timestamp ASC")
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(Long senderId, Long receiverId);

    @Query("SELECT DISTINCT CASE WHEN sender_id = :userId THEN receiver_id ELSE sender_id END AS contactId " +
            "FROM messages WHERE sender_id = :userId OR receiver_id = :userId")
    List<Long> findConversationUserIds(Long userId);
}