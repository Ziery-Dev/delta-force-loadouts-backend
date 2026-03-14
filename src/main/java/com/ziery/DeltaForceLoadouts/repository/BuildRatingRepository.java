package com.ziery.DeltaForceLoadouts.repository;


import com.ziery.DeltaForceLoadouts.entity.Build;
import com.ziery.DeltaForceLoadouts.entity.BuildRating;
import com.ziery.DeltaForceLoadouts.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuildRatingRepository extends JpaRepository<BuildRating, Long> {
    Optional<BuildRating> findByUserAndBuild(User user, Build build);

    boolean existsByBuildIdAndUserIdAndRating(Long buildId, Long userId, int rating);


    //Deleção manual da avaliação associada a uma build (para remover a build sem erro de chave estrageira)
    @Modifying
    @Query("delete from BuildRating r where r.build.id = :buildId")
    void deleteByBuildId(@Param("buildId") Long buildId);

}
