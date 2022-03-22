package com.example.chatserver.repo;

import com.example.chatserver.models.ChatMessage;
import com.example.chatserver.models.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    long countBySenderIdAndRecipientIdAndStatus(UUID senderId, UUID recipientId, MessageStatus status);
    List<ChatMessage> findByChatId(UUID chatId);
}
