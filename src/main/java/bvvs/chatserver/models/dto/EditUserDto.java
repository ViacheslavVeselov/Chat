package bvvs.chatserver.models.dto;

import lombok.Data;

@Data
public class EditUserDto {
    private String name;
    private String email;
    private String password;
    private String photoPathToFile;
}
