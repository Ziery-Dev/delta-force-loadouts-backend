package com.ziery.DeltaForceLoadouts.entity;

import com.ziery.DeltaForceLoadouts.security.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "Builds")
public class Build {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;


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

    //Armazena a hora de criação da build
    private Instant createdAt = Instant.now();


    @OneToMany(mappedBy = "build")
    private List<BuildRating> ratings;


    //Identificam se o usuário ja avaliou a build e se deu like ou dislike
    public boolean isLikedBy(User user) {
        if (ratings == null) return false;
        return ratings.stream().anyMatch(r ->
                r.getUser().getId().equals(user.getId()) && r.getRating() == 1
        );
    }

    public boolean isDislikedBy(User user) {
        if (ratings == null) return false;
        return ratings.stream().anyMatch(r ->
                r.getUser().getId().equals(user.getId()) && r.getRating() == -1
        );
    }






}
