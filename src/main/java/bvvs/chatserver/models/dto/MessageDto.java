package bvvs.chatserver.models.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MessageDto {
    private String text;

    private String pathToFile;

    private UUID chatId;

    private UUID userId;
}
