package com.ziery.DeltaForceLoadouts.domain.specification;

import com.ziery.DeltaForceLoadouts.entity.Build;
import com.ziery.DeltaForceLoadouts.entity.BuildRange;
import jakarta.persistence.criteria.Join;
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

    //para buscar arma ou nome do cridor na barra de pesquisa
    public static Specification<Build> search(String termo) {
        return (root, query, cb) -> {
            if (termo == null || termo.isBlank()) {
                return cb.conjunction();
            }

            String like = "%" + termo.toLowerCase() + "%";

            Join<Object, Object> creator = root.join("creator");
            Join<Object, Object> weapon = root.join("weapon");

            return cb.or(
                    cb.like(cb.lower(creator.get("username")), like),
                    cb.like(cb.lower(weapon.get("name")), like)
            );
        };
    }
}
