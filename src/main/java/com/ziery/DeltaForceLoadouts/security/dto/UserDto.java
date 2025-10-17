package com.ziery.DeltaForceLoadouts.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;

    public UserDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
