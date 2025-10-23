package com.ziery.DeltaForceLoadouts.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Builds")
public class Build {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80, nullable = false, unique = true)
    private String code;
    @Column(length = 250, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private BuildRange distance_range;


    @ManyToOne
    @JoinColumn(name = "weapon_id")
    private Weapon weapon;



}
