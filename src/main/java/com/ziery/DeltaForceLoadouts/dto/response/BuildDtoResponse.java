package com.ziery.DeltaForceLoadouts.dto.response;

import com.ziery.DeltaForceLoadouts.entity.Build;
import com.ziery.DeltaForceLoadouts.entity.BuildRange;
import com.ziery.DeltaForceLoadouts.security.entity.User;

import java.time.Instant;

public record BuildDtoResponse(
        Long id,
        String creatorUsername,
        String code,
        String description,
        BuildRange distance_range,
        Integer weaponId,
        Long creatorId,
        Instant createdAt,

        Boolean likedByUser,
        Boolean dislikedByUser
) {
    public BuildDtoResponse(Build build) {
        this(
                build.getId(),
                extractCreatorUsername(build.getCreator()),
                build.getCode(),
                build.getDescription(),
                build.getDistance_range(),
                build.getWeapon().getId(),
                build.getCreator().getId(),
                build.getCreatedAt(),
                null,  // likedByUser
                null   // dislikedByUser
        );
    }


    // Construtor original (para compatibilidade)
    public BuildDtoResponse(Build build, User currentUser) {
        this(
                build.getId(),
                extractCreatorUsername(build.getCreator()),
                build.getCode(),
                build.getDescription(),
                build.getDistance_range(),
                build.getWeapon().getId(),
                build.getCreator().getId(),
                build.getCreatedAt(),
                build.isLikedBy(currentUser),
                build.isDislikedBy(currentUser)
        );
    }

    public BuildDtoResponse(Build build, boolean likedByUser, boolean dislikedByUser) {
        this(
                build.getId(),
                extractCreatorUsername(build.getCreator()),
                build.getCode(),
                build.getDescription(),
                build.getDistance_range(),
                build.getWeapon().getId(),
                build.getCreator().getId(),
                build.getCreatedAt(),
                likedByUser,
                dislikedByUser
        );
    }

    private static String extractCreatorUsername(User creator) {
        return (creator != null) ? creator.getUsername() : "Desconhecido";
    }

}
