package bvvs.chatserver.models.dto;

import lombok.Data;

@Data
public class ChatJoinDto {
    private String userId;
    private String isChatAdmin;
}
