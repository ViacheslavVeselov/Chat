package com.example.chatserver.service;

import com.example.chatserver.models.ChatRoom;
import com.example.chatserver.repo.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ChatRoomService {
    @Autowired private ChatRoomRepository chatRoomRepository;

    public Optional<UUID> getChatId(UUID senderId, UUID recipientId, boolean createIfNotExist) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getId)
                .or(() -> {
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                    UUID chatId = UUID.randomUUID(); //TODO make sure ids are different

                    ChatRoom senderRecipient = new ChatRoom(chatId, senderId, recipientId);
                    ChatRoom recipientSender = new ChatRoom(chatId, recipientId, senderId);
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }
}
