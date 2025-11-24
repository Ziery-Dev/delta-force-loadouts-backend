package com.ziery.DeltaForceLoadouts.repository;


import com.ziery.DeltaForceLoadouts.entity.Build;
import com.ziery.DeltaForceLoadouts.entity.BuildRating;
import com.ziery.DeltaForceLoadouts.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuildRatingRepository extends JpaRepository<BuildRating, Long> {
    Optional<BuildRating> findByUserAndBuild(User user, Build build);

    boolean existsByBuildIdAndUserIdAndRating(Long buildId, Long userId, int rating);

}
