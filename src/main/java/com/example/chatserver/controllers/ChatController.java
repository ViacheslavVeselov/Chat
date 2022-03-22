package com.example.chatserver.controllers;

import com.example.chatserver.models.Chat;
import com.example.chatserver.models.ChatMessage;
import com.example.chatserver.models.ChatNotification;
import com.example.chatserver.service.ChatMessageService;
import com.example.chatserver.service.ChatRoomService;
import com.example.chatserver.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
public class ChatController {
    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private ChatMessageService chatMessageService;
    @Autowired private ChatService chatService;
    @Autowired private ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        Optional<UUID> chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());

        ChatMessage saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId().toString(),"/queue/messages",
                new ChatNotification(
                        saved.getId().toString(),
                        saved.getSenderId().toString(),
                        "saved.getSenderName())")); //TODO select from user table
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(
            @PathVariable UUID senderId,
            @PathVariable UUID recipientId
    ) {
        return ResponseEntity.ok(chatMessageService.countNewMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessages(
            @PathVariable UUID senderId,
            @PathVariable UUID recipientId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> findMessage(@PathVariable UUID id) {
        return ResponseEntity.ok(chatMessageService.findById(id));
    }

    @GetMapping("/chats/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getChat(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok(chatService.findById(id));
    }

    @PostMapping("/chats")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Chat> addChat(
            @RequestParam String title/*, @RequestParam String[] userIds,*/
    ) {
        return ResponseEntity.ok(chatService.save(title));
    }

    @DeleteMapping("/chats/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChat(@PathVariable(value = "id") UUID id) {
        chatService.deleteById(id);
    }
}