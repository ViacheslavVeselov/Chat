package bvvs.chatserver.models.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ChatJoinDto {
    private UUID userId;

    private boolean isChatAdmin;
}
