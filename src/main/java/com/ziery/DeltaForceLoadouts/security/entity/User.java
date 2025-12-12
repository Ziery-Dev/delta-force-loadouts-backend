package com.ziery.DeltaForceLoadouts.security.entity;

import com.ziery.DeltaForceLoadouts.entity.Build;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoles role;

    @ManyToMany
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "build_id")
    )
    private Set<Build> favoriteBuilds = new LinkedHashSet<>();




    public void addFavoriteBuild(Build build) {
        this.favoriteBuilds.add(build);
    }

    public void removeFavoriteBuild(Build build) {
        this.favoriteBuilds.remove(build);
    }


}
