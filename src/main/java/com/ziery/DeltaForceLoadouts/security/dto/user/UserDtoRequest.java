package com.ziery.DeltaForceLoadouts.security.dto.user;

import com.ziery.DeltaForceLoadouts.security.entity.UserRoles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoRequest {
    @NotBlank (message = "Insira o nome de usuário!") @Size(max = 30, min = 3, message = "O nome de usuário deve ter entre 3 e 30 caracteres!")
    private String username;
    @NotBlank (message = "Insira a senha de usuário!") @Size(max = 72, min = 6, message = "A senha deve ter entre 6 e 72 carateres!")
    private String password;
    @NotNull (message = "Defina um perfil de usuário (role)!")
    private UserRoles role;

}
