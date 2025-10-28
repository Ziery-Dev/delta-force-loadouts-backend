package com.ziery.DeltaForceLoadouts.dto.request;

import com.ziery.DeltaForceLoadouts.entity.OperatorCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OperatorDtoRequest (@NotBlank(message = "Preencha o nome") @Size(max = 30, message = "O tamanho máximo do nome é de 30 caracteres") String name, @NotNull(message = "Preencha a categoria") @Size(max = 15, message = "O tamanho máximo da categoria é de 15 caracteres") OperatorCategory category) {



}