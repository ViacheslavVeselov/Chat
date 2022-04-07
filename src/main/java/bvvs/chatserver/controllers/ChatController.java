package bvvs.chatserver.controllers;

import bvvs.chatserver.models.Chat;
import bvvs.chatserver.models.dto.ChatJoinDto;
import bvvs.chatserver.models.dto.CreateGroupChatDto;
import bvvs.chatserver.models.dto.EditChatSettingsDto;
import bvvs.chatserver.service.ChatFacade;
import bvvs.chatserver.models.dto.MessageDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/chats", produces = "application/hal+json")
@AllArgsConstructor
public class ChatController {
    private ChatFacade chatFacade;

    @PostMapping("/direct/{userId}/send")
    @ResponseStatus(CREATED)
    public void sendDirectMessage(@RequestBody MessageDto messageDto, @PathVariable UUID userId) {
        chatFacade.sendDirectMessage(messageDto, userId);
    }

    @PostMapping("/group/{groupId}/send")
    @ResponseStatus(CREATED)
    public void sendGroupMessage(@RequestBody MessageDto messageDto, @PathVariable UUID groupId) {
        chatFacade.sendGroupMessage(messageDto, groupId);
    }

    @GetMapping("/{chatId}")
    @ResponseStatus(OK)
    public Chat getChat(@PathVariable UUID chatId) {
        return chatFacade.getChat(chatId);
    }

    // method requires to add createdBy to userIds
    @PostMapping("/group")
    @ResponseStatus(CREATED)
    public Chat createGroupChat(@RequestBody CreateGroupChatDto createGroupChatDto) {
        return chatFacade.createGroupChat(createGroupChatDto);
    }

    @PostMapping("/group/{groupId}/join")
    @ResponseStatus(CREATED)
    public Chat joinGroupChat(@PathVariable UUID groupId, @RequestBody ChatJoinDto chatJoinDto) {
        return chatFacade.joinGroupChat(groupId, chatJoinDto);
    }

    @DeleteMapping("/group/{groupId}/{userId}/leave")
    @ResponseStatus(NO_CONTENT)
    public void leaveGroupChat(@PathVariable UUID groupId, @PathVariable UUID userId) {
        chatFacade.leaveGroupChat(groupId, userId);
    }

    @DeleteMapping("/{chatId}/delete")
    @ResponseStatus(NO_CONTENT)
    public void deleteChat(@PathVariable UUID chatId) {
        chatFacade.deleteChat(chatId);
    }

    @PutMapping("/{chatId}/{userId}/edit")
    @ResponseStatus(OK)
    public Chat editChatSettings(
            @PathVariable UUID chatId,
            @PathVariable UUID userId,
            @RequestBody EditChatSettingsDto editChatSettingsDto
    ) {
        return chatFacade.editChatSettings(chatId, userId, editChatSettingsDto);
    }
}