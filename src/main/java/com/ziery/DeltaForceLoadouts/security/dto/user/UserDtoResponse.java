package com.ziery.DeltaForceLoadouts.security.dto.user;

import com.ziery.DeltaForceLoadouts.security.entity.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoResponse {
    private Long id;
    private String username;
    private String email;
    private UserRoles role;
    private Boolean enabled;


    public UserDtoResponse(String username) {
    }

    public UserDtoResponse(Long id, String username, String email, UserRoles role) {}
}
