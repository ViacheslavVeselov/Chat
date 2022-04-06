package bvvs.chatserver.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ChatDto {
    private String title;

    private List<UUID> userIds;
}
