package com.infraredcoding.cubesturm.domain.dto.auth;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String name;
    private String wcaId;
    private String username;
    private String email;
    private String password;
}
