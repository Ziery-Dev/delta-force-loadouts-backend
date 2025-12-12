package com.ziery.DeltaForceLoadouts.security.dto.user;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank (message = "Insira um e-mail para recuperação de senha") @Size(max = 254, min = 5, message = "O e-mail deve ter entre 5 e 254 caracteres!")
    private String email;
    @NotBlank (message = "Insira a senha de usuário!") @Size(max = 72, min = 6, message = "A senha deve ter entre 6 e 72 carateres!")
    private String password;


}
