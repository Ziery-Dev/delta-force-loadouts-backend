package com.ziery.DeltaForceLoadouts.entity;

import com.ziery.DeltaForceLoadouts.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

//Entidade responsável em lidar coms o likes e dislikes das builds
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "BuildRatings")
public class BuildRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Build build;


    private int rating;


    public BuildRating(User user, Build build, int value) {
        this.user = user;
        this.build = build;
        this.rating = value;
    }
}
