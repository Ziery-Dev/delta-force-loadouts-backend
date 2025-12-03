package com.ziery.DeltaForceLoadouts.domain.specification;

import com.ziery.DeltaForceLoadouts.entity.Build;
import com.ziery.DeltaForceLoadouts.entity.BuildRange;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class BuildSpec {

    public static Specification<Build> byDistanceRange(BuildRange distance) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(distance)) {
                return builder.conjunction(); //retorna tudo sem filtro
            }
            return builder.equal(root.get("distance_range"), distance);
        };
    }
}
