package bvvs.chatserver.models.dto;

import lombok.Data;

@Data
public class EditChatSettingsDto {
   private boolean banned;
   private boolean sendNotifications;
}
