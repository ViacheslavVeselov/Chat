package bvvs.chatserver.service;

import bvvs.chatserver.models.ChatMessage;
import bvvs.chatserver.models.User;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WsSenderService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendDirectMessage(User user, ChatMessage chatMessage) {

        messagingTemplate.convertAndSendToUser(user.getId().toString(), "/queue/messages", chatMessage);
    }
}
