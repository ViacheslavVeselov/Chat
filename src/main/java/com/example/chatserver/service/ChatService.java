package com.example.chatserver.service;

import com.example.chatserver.exception.ResourceNotFoundException;
import com.example.chatserver.models.Chat;
import com.example.chatserver.repo.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatService {
    @Autowired private ChatRepository repository;

    public Chat findById(UUID id) {
        return repository.findById(id)
                .map(chat -> repository.save(chat))
                .orElseThrow(() -> new ResourceNotFoundException("can't find chat (" + id + ")"));
    }

    public Chat save(String title) {
        Chat chat = new Chat(title/*TODO , userIds*/);
        return repository.save(chat);
    }

    public void deleteById(UUID id) {
        Chat chat = repository.findById(id).orElseThrow(IllegalStateException::new);
        repository.delete(chat);
    }
}
