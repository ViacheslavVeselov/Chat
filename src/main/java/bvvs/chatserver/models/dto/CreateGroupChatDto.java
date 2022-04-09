package bvvs.chatserver.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateGroupChatDto {
    private String title;
    private String createdBy;
    private List<String> userIds;
}
