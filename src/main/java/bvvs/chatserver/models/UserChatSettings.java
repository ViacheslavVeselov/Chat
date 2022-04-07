package bvvs.chatserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "chat_user_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChatSettings {
    @EmbeddedId
    private UserChatSettingsCompositeId id;

    public UserChatSettings(Chat chat, User user) {
        this.chat = chat;
        this.user = user;
        this.id = new UserChatSettingsCompositeId(chat.getId(), user.getId());
    }

    public UserChatSettings(Chat chat, User user, Boolean isChatAdmin) {
        this.chat = chat;
        this.user = user;
        this.isChatAdmin = isChatAdmin;
        this.id = new UserChatSettingsCompositeId(chat.getId(), user.getId());
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("chatId")
    @JsonBackReference
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JsonBackReference
    private User user;

    @Column(name="send_notifications")
    private Boolean sendNotifications = false;

    @Column(name = "banned")
    private Boolean banned = false;

    @Column(name = "is_chat_admin")
    private Boolean isChatAdmin = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserChatSettings that = (UserChatSettings) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
