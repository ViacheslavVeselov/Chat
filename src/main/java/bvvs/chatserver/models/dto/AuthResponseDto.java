package bvvs.chatserver.models.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthResponseDto {
    private String token;

    private String userId;
}
