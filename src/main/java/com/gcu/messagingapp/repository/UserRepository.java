package com.gcu.messagingapp.repository;

import com.gcu.messagingapp.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
    @Query("SELECT * FROM users WHERE id != :userId")
    List<User> findAllExceptUser(Long userId);

    @Query("SELECT u.* FROM users u JOIN friends f ON u.id = f.friend_id WHERE f.user_id = :userId")
    List<User> findFriendsByUserId(Long userId);
}