package com.ziery.DeltaForceLoadouts.repository;

import com.ziery.DeltaForceLoadouts.entity.Operator;
import com.ziery.DeltaForceLoadouts.entity.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeaponRepository extends JpaRepository<Weapon, Integer> {

    Optional<Weapon> findByName(String name);


    //Verifica a quantidade de armas que existem cadastradas com o operador atual
    long countByCompatibleOperators_Id(Integer operatorId);
}
