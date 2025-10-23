package com.ziery.DeltaForceLoadouts.entity;

import lombok.Getter;

@Getter
public enum OperatorCategory {

        ASSALTO ("Assalto"),
        SUPORTE ("Suporte"),
        ENGENHEIRO ("Engenheiro"),
        RECONHECIMENTO ("Reconhecimento");

        private final String category;

        OperatorCategory(String category) {
            this.category = category;
        }


}


