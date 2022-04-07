package bvvs.chatserver.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Setter
@With
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
@Table(name = "chat")
public class Chat {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User user;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UserChatSettings> userChatSettingsList = new ArrayList<>();

    public void addUser(User user) {
        UserChatSettings ucs = new UserChatSettings(this, user);
        userChatSettingsList.add(ucs);
        user.getUserChatSettings().add(ucs);
    }

    public void addUserAndSetIsChatAdmin(User user, Boolean isChatAdmin) {
        UserChatSettings ucs = new UserChatSettings(this, user, isChatAdmin);
        userChatSettingsList.add(ucs);
        user.getUserChatSettings().add(ucs);
    }

    public void deleteUser(User user) {
        List<UserChatSettings> ucsList = getUserChatSettingsList();
        UserChatSettings deleteUcs = null;

        for (UserChatSettings ucs : ucsList) {
            if (ucs.getUser().getId().equals(user.getId()) && ucs.getChat().getId().equals(getId())) {
                deleteUcs = ucs;
            }
        }

        if (deleteUcs != null) {
            ucsList.remove(deleteUcs);
            user.getUserChatSettings().remove(deleteUcs);
        }
    }

    public void editChatSettings(User user, boolean banned, boolean sendNotifications) {
        List<UserChatSettings> ucsList = getUserChatSettingsList();

        for (UserChatSettings ucs : ucsList) {
            if (ucs.getUser().getId().equals(user.getId()) && ucs.getChat().getId().equals(getId())) {
                ucs.setBanned(banned);
                ucs.setSendNotifications(sendNotifications);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return id.equals(chat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
