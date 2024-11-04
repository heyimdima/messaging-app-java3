package com.gcu.messagingapp.controller;

import com.gcu.messagingapp.model.Message;
import com.gcu.messagingapp.model.User;
import com.gcu.messagingapp.service.MessagingService;
import com.gcu.messagingapp.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessagingController {
    private final MessagingService messagingService;
    private final UserRepository userRepository;

    public MessagingController(MessagingService messagingService, UserRepository userRepository) {
        this.messagingService = messagingService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showMessages(@AuthenticationPrincipal UserDetails currentUser, @RequestParam(required = false) Long conversationId, Model model) {
        User user = userRepository.findByUsername(currentUser.getUsername());
        List<User> conversations = messagingService.getConversations(user.getId());

        List<User> allUsers = messagingService.getAllUsersExceptCurrent(user.getId());
        List<User> nonFriends = allUsers.stream()
                .filter(u -> !messagingService.getFriends(user.getId()).contains(u))
                .toList();

        List<Message> messages = (conversationId != null)
                ? messagingService.getMessagesBetweenUsers(user.getId(), conversationId)
                : Collections.emptyList();

        model.addAttribute("nonFriends", nonFriends);
        model.addAttribute("conversations", conversations);
        model.addAttribute("messages", messages);
        model.addAttribute("selectedConversationId", conversationId);
        model.addAttribute("currentUserId", user.getId());
        model.addAttribute("friends", messagingService.getFriends(user.getId()));
        model.addAttribute("allUsers", messagingService.getAllUsersExceptCurrent(user.getId()));
        return "messages";
    }

    @PostMapping("/addFriend")
    public String addFriend(@AuthenticationPrincipal UserDetails currentUser, @RequestParam Long friendId) {
        User user = userRepository.findByUsername(currentUser.getUsername());
        messagingService.addFriend(user.getId(), friendId);
        return "redirect:/messages";
    }

    @PostMapping("/send")
    public String sendMessage(@AuthenticationPrincipal UserDetails currentUser, @RequestParam Long receiverId, @RequestParam String content) {
        User sender = userRepository.findByUsername(currentUser.getUsername());
        messagingService.sendMessage(sender.getId(), receiverId, content);
        return "redirect:/messages?conversationId=" + receiverId;
    }
}