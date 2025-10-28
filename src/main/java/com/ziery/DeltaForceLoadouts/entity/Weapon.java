package com.ziery.DeltaForceLoadouts.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@Table(name = "Weapons")
public class Weapon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 15, nullable = false, unique = true)
    private String name;

    @Column(length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private WeaponCategory category;


    @ManyToMany
    @JoinTable(
            name = "weapon_operator_compatibility", // Nome da tabela intermediária no banco
            joinColumns = @JoinColumn(name = "weapon_id"), // Coluna que referencia a Weapon na tabela intermediária
            inverseJoinColumns = @JoinColumn(name = "operator_id") // Coluna que referencia o Operator na tabela intermediária
    )
    @EqualsAndHashCode.Exclude
    private Set<Operator> compatibleOperators = new HashSet<>();

}
