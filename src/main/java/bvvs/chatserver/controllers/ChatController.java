package bvvs.chatserver.controllers;

import bvvs.chatserver.service.ChatFacade;
import bvvs.chatserver.models.dto.MessageDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

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

    }
}