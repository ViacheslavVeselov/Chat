package bvvs.chatserver.service;

import bvvs.chatserver.repo.ChatMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private WsSenderService wsSenderService;


  /*  public long countNewMessages(UUID senderId, UUID recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(senderId, recipientId, MessageStatus.RECEIVED);
    }
*/
 /*   public List<ChatMessage> findChatMessages(UUID senderId, UUID recipientId) {
        Optional<UUID> chatId = chatRoomService.getChatId(senderId, recipientId, false);
        List<ChatMessage> messages = chatId.map(cId -> repository.findByChatId(cId)).orElse(new ArrayList<>());

        if(messages.size() > 0) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }

        return messages;
    }

    public ChatMessage findById(UUID id) {
        return repository.findById(id)
                .map(chatMessage -> {
                    return repository.save(chatMessage);
                })
                .orElseThrow(() -> new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    public void updateStatuses(UUID senderId, UUID recipientId, MessageStatus status) {
        //TODO update statuses with Postgres
    }*/
}
