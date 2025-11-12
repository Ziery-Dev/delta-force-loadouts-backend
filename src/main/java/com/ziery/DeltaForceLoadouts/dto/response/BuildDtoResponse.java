package com.ziery.DeltaForceLoadouts.dto.response;

import com.ziery.DeltaForceLoadouts.entity.Build;
import com.ziery.DeltaForceLoadouts.entity.BuildRange;
import com.ziery.DeltaForceLoadouts.security.entity.User;

public record BuildDtoResponse(Long id, String creatorUsername, String code, String description, BuildRange distance_range, Integer weaponId,   Long creatorId ) {
    public BuildDtoResponse (Build build){
        this(
                build.getId(),
                extractCreatorUsername(build.getCreator()),
                build.getCode(),
                build.getDescription(),
                build.getDistance_range(),
                build.getWeapon().getId(),
                build.getCreator().getId()
        );
    }

    private static String extractCreatorUsername(User creator) {
        return (creator != null) ? creator.getUsername() : "Desconhecido";
    }

}