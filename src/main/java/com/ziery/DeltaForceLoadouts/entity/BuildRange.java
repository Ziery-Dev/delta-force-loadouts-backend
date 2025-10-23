package com.ziery.DeltaForceLoadouts.entity;

import lombok.Getter;

@Getter
public enum BuildRange {
    CURTO("Curta distância"),
    MEDIO("Média distância"),
    LONGE ("Longa distância"),
    MUITO_LONGE("Muito longa");

    private final String label;


    BuildRange(String label) {
        this.label = label;
    }

}
