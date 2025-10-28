package com.ziery.DeltaForceLoadouts.dto.request;

import com.ziery.DeltaForceLoadouts.entity.WeaponCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public record WeaponDtoRequest(@NotBlank(message = "Preencha o nome da arma") @Size(max = 15, message = "O tamanho máximo da arma é de 15 caracteres") String name, @NotNull(message = "Preencha a categoria da arma") WeaponCategory category, @NotNull(message = "Informa os operadores compativeis com a arma") Set<Integer> operatorIds) {

}