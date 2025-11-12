package com.ziery.DeltaForceLoadouts.dto.request;

import com.ziery.DeltaForceLoadouts.entity.OperatorCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OperatorDtoRequest (@NotBlank(message = "Preencha o nome do operador") @Size(max = 30, message = "O tamanho máximo do nome é de 30 caracteres") String name, @NotNull(message = "Escolha uma a categoria")  OperatorCategory category) {



}