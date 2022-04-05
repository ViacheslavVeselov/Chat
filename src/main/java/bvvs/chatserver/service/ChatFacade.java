package bvvs.chatserver.service;

import bvvs.chatserver.exception.ValidationException;
import bvvs.chatserver.models.Chat;
import bvvs.chatserver.models.ChatMessage;
import bvvs.chatserver.models.User;
import bvvs.chatserver.models.dto.MessageDto;
import bvvs.chatserver.repo.ChatMessageRepository;
import bvvs.chatserver.repo.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class ChatFacade {
    private final ChatRepository chatRepository;
    private final ChatMessageRepository messageRepository;
    private final UserService userService;
    private final WsSenderService wsSenderService;

    public void sendDirectMessage(MessageDto messageDto, UUID recipientId) {

        User sender = userService.tryGetUserById(messageDto.getUserId());
        User recipient = userService.tryGetUserById(recipientId);
        ChatMessage chatMessage = ChatMessage.from(messageDto, sender);
        UUID chatId = chatMessage.getChatId();

        Chat chat;
        if (chatId == null) {
            chat = new Chat();
            chat.addUser(sender);
            chat.addUser(recipient);
        } else {
            chat = chatRepository.findById(chatId).orElseThrow(() -> new ValidationException(Map.of("No such chat!",
                    chatId.toString())));
        }

        wsSenderService.sendDirectMessage(recipient,
                messageRepository.save(chatMessage.withChatId(chatRepository.save(chat).getId())));
    }


}
