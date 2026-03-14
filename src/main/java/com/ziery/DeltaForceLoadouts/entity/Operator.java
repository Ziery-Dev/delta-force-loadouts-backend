package com.ziery.DeltaForceLoadouts.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "operators")
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private OperatorCategory category;

    @ManyToMany(mappedBy = "compatibleOperators")
    @EqualsAndHashCode.Exclude
    private Set<Weapon> compatibleWeapons = new HashSet<>();



}
