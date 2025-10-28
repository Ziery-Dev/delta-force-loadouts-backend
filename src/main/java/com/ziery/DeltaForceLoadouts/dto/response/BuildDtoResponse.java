package com.ziery.DeltaForceLoadouts.dto.response;

import com.ziery.DeltaForceLoadouts.entity.Build;
import com.ziery.DeltaForceLoadouts.entity.BuildRange;

public record BuildDtoResponse(Long id, String code, String description, BuildRange distance_range, Integer weaponId) {
    public BuildDtoResponse (Build build){
        this(
                build.getId(),
                build.getCode(),
                build.getDescription(),
                build.getDistance_range(),
                build.getWeapon().getId()
        );
    }

}