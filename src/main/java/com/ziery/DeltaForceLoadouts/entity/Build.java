package com.ziery.DeltaForceLoadouts.entity;

import com.ziery.DeltaForceLoadouts.security.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "builds")
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
    @Column(length = 15, nullable = false, name = "distance_range")
    private BuildRange distanceRange;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weapon_id", nullable = false)
    private Weapon weapon;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

    private Long likeCount = 0L;

    private Long dislikeCount = 0L;


    @OneToMany(mappedBy = "build")
    private List<BuildRating> ratings;

    @ManyToMany(mappedBy = "favoriteBuilds")
    private Set<User> favoriteUsers = new HashSet<>();



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
