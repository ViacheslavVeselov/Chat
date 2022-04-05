package bvvs.chatserver.models.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Incorrect email")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 5, max = 10)
    private String password;

    @NotBlank(message = "Role ID cannot be empty")
    private String roleId;

    private String photoPathToFile;
}
