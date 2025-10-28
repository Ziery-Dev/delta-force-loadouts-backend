package com.ziery.DeltaForceLoadouts.repository;

import com.ziery.DeltaForceLoadouts.entity.Operator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperatorRepository extends JpaRepository<Operator, Integer> {
    Optional <Operator>findByName(String name);
}
