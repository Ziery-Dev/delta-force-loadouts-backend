package com.ziery.DeltaForceLoadouts.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "Operators")
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 30, nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private OperatorCategory category;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    @ManyToMany(mappedBy = "campatibleOperators")
    @EqualsAndHashCode.Exclude
    private Set<Weapon> compatibleWeapons = new HashSet<>();



}
