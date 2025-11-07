package com.ziery.DeltaForceLoadouts.dto.response;

import com.ziery.DeltaForceLoadouts.entity.Operator;
import com.ziery.DeltaForceLoadouts.entity.Weapon;
import com.ziery.DeltaForceLoadouts.entity.WeaponCategory;

import java.util.Set;
import java.util.stream.Collectors;

public record WeaponDtoResponse(
        Integer id,
        String name,
        WeaponCategory category,
        String imgUrl,
        Set<Integer> operatorIds

) {

    //Aqui o construtor pega o Operador compativel que é atributo de arma e responde no dto somente com seu Id
    public WeaponDtoResponse(Weapon weapon) {
        this(
                weapon.getId(),
                weapon.getName(),
                weapon.getCategory(),
                weapon.getImgUrl(),
                weapon.getCompatibleOperators()
                        .stream()
                        .map(Operator::getId)
                        .collect(Collectors.toSet())
        );
    }
}

