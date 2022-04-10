package bvvs.chatserver.service;

import bvvs.chatserver.exception.ValidationException;
import bvvs.chatserver.models.Chat;
import bvvs.chatserver.models.ChatMessage;
import bvvs.chatserver.models.User;
import bvvs.chatserver.models.UserChatSettings;
import bvvs.chatserver.models.dto.ChatJoinDto;
import bvvs.chatserver.models.dto.CreateGroupChatDto;
import bvvs.chatserver.models.dto.EditChatSettingsDto;
import bvvs.chatserver.models.dto.MessageDto;
import bvvs.chatserver.repo.ChatMessageRepository;
import bvvs.chatserver.repo.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class ChatFacade {
    private final ChatRepository chatRepository;
    private final ChatMessageRepository messageRepository;
    private final UserService userService;
    private final WsSenderService wsSenderService;
    @Autowired private SendEmailService sendEmailService;

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
        sendEmailFromOneToOneChatIfNeeded(sender, recipient, chat, chatMessage.getText());
    }

    public void sendGroupMessage(MessageDto messageDto, UUID groupId) {
        User sender = userService.tryGetUserById(messageDto.getUserId());
        ChatMessage chatMessage = ChatMessage.from(messageDto, sender);
        Chat chat = chatRepository.findById(groupId).orElseThrow(() -> new ValidationException(Map.of("No such chat!",
                groupId.toString())));

        wsSenderService.sendGroupMessage(chat,
                messageRepository.save(chatMessage.withChatId(chatRepository.save(chat).getId())));
        sendEmailFromGroupChatIfNeeded(sender, chat, chatMessage.getText());
    }

    public Map<String, Object> getChatWithMessages(UUID chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ValidationException(Map.of("No such chat!",
                chatId.toString())));

        List<ChatMessage> messages = messageRepository.findByChatId(chatId);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("chat", chat);
        response.put("messages", messages);
        return response;
    }

    public Chat createGroupChat(CreateGroupChatDto createGroupChatDto) {
        Chat chat = new Chat();
        chat.setTitle(createGroupChatDto.getTitle());
        User createdBy = userService.tryGetUserById(UUID.fromString(createGroupChatDto.getCreatedBy()));
        chat.setUser(createdBy);

        for (String userId : createGroupChatDto.getUserIds()) {
            User user = userService.tryGetUserById(UUID.fromString(userId));
            chat.addUser(user);
        }

        return chatRepository.save(chat);
    }

    public Map<String, Object> joinGroupChat(UUID groupId, ChatJoinDto chatJoinDto) {
        Chat chat = chatRepository.findById(groupId).orElseThrow(() -> new ValidationException(Map.of("No such chat!",
                groupId.toString())));
        UUID userId = UUID.fromString(chatJoinDto.getUserId());
        User user = userService.tryGetUserById(userId);
        chat.addUserAndSetIsChatAdmin(user, Boolean.parseBoolean(chatJoinDto.getIsChatAdmin()));

        List<ChatMessage> messages = messageRepository.findByChatId(chat.getId());
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("chat", chat);
        response.put("messages", messages);
        return response;
    }

    public void leaveGroupChat(UUID groupId, UUID userId) {
        Chat chat = chatRepository.findById(groupId).orElseThrow(() -> new ValidationException(Map.of("No such chat!",
                groupId.toString())));
        User user = userService.tryGetUserById(userId);
        chat.deleteUser(user);
    }

    public void deleteChat(UUID chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ValidationException(Map.of("No such chat!",
                chatId.toString())));
        chatRepository.delete(chat);
    }

    public Map<String, Object> editChatSettings(UUID chatId, UUID userId, EditChatSettingsDto editChatSettingsDto) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ValidationException(Map.of("No such chat!",
                chatId.toString())));
        User user = userService.tryGetUserById(userId);
        boolean banned = editChatSettingsDto.isBanned();
        boolean sendNotifications = editChatSettingsDto.isSendNotifications();
        chat.editChatSettings(user, banned, sendNotifications);

        List<ChatMessage> messages = messageRepository.findByChatId(chat.getId());
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("chat", chat);
        response.put("messages", messages);
        return response;
    }

    // this method needs spring.jackson.serialization.fail-on-empty-beans=false in application.properties
    public List<Chat> getChats(User user) {
        List<UserChatSettings> ucsList = user.getUserChatSettings();
        List<UUID> chatsIds = new ArrayList<>();

        for (UserChatSettings ucs : ucsList) {
            UUID chatId = ucs.getChat().getId();
            chatsIds.add(chatId);
        }
        return (List<Chat>) chatRepository.findAllById(chatsIds);
    }

    public void sendEmailFromOneToOneChatIfNeeded(User sender, User recipient, Chat chat, String messageText) {
        if (!sender.isBanned() && !recipient.isBanned()) {
            List<UserChatSettings> chatUcsList = chat.getUserChatSettingsList();
            UserChatSettings senderUcs = null;
            UserChatSettings recipientUcs = null;

            for (UserChatSettings chatUcs : chatUcsList) {
                if (chatUcs.getUser().getId().equals(sender.getId())) senderUcs = chatUcs;
                if (chatUcs.getUser().getId().equals(recipient.getId())) recipientUcs = chatUcs;
            }

            if (senderUcs != null && recipientUcs != null && senderUcs.getBanned().equals(false)
                    && recipientUcs.getBanned().equals(false) && recipientUcs.getSendNotifications().equals(true))
                sendEmailService.sendEmail(recipient.getEmail(), messageText);
        }
    }

    private void sendEmailFromGroupChatIfNeeded(User sender, Chat chat, String messageText) {
        if (!sender.isBanned()) {
            List<UserChatSettings> chatUcsList = chat.getUserChatSettingsList();
            List<UserChatSettings> recipientsUcsList = new ArrayList<>();
            UserChatSettings senderUcs = null;

            for (UserChatSettings chatUcs : chatUcsList) {
                if (chatUcs.getUser().getId().equals(sender.getId())) senderUcs = chatUcs;
                else recipientsUcsList.add(chatUcs);
            }

            if (senderUcs != null && !recipientsUcsList.isEmpty() && senderUcs.getBanned().equals(false)) {
                for (UserChatSettings recipientUcs : recipientsUcsList) {
                    User recipient = userService.tryGetUserById(recipientUcs.getUser().getId());
                    if (!recipient.isBanned() && recipientUcs.getBanned().equals(false) && recipientUcs.getSendNotifications().equals(true))
                        sendEmailService.sendEmail(recipient.getEmail(), messageText);
                }
            }
        }
    }
}
