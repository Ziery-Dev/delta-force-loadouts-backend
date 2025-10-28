package com.ziery.DeltaForceLoadouts.dto.request;

import com.ziery.DeltaForceLoadouts.entity.BuildRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BuildDtoRequest(@NotBlank(message = "Prencha o campo código de build") @Size(max = 80, message = "O tamanho máximo do código é 80 caracteres") String code,
                              @NotBlank (message = "Descreva a as informações build") @Size(max = 250, message = "O tamanho máximo da descrição é de 250 caracteres") String description,
                              @NotNull (message = "Preencha o campo de alcance da arma") BuildRange distance_range,
                              @NotNull(message = "Selecione a arma da qual a build pertence") Integer weaponId) {


}

