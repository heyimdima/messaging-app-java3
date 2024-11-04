package com.gcu.messagingapp.service;

import com.gcu.messagingapp.model.Message;
import com.gcu.messagingapp.model.User;
import com.gcu.messagingapp.repository.MessageRepository;
import com.gcu.messagingapp.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessagingService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    public MessagingService(MessageRepository messageRepository, UserRepository userRepository, JdbcTemplate jdbcTemplate) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    // Fetch all unique conversations for the user
    public List<User> getConversations(Long userId) {
        List<Long> contactIds = messageRepository.findConversationUserIds(userId);
        return (List<User>) userRepository.findAllById(contactIds);
    }

    // Fetch all friends of the user
    public List<User> getFriends(Long userId) {
        return userRepository.findFriendsByUserId(userId);
    }

    // Fetch all users except the current user
    public List<User> getAllUsersExceptCurrent(Long userId) {
        return userRepository.findAllExceptUser(userId);
    }

    // Add a friend using JdbcTemplate
    public void addFriend(Long userId, Long friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    // Start a conversation with a friend
    public List<Message> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(senderId, receiverId);
    }

    // Send a message
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }
}