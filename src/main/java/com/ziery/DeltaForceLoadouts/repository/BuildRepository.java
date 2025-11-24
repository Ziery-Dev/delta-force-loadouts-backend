package com.ziery.DeltaForceLoadouts.repository;

import com.ziery.DeltaForceLoadouts.entity.Build;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BuildRepository extends JpaRepository<Build, Long> {
    Optional<Build> findByCode(String code);

    List<Build> findByCreatorId(Long creatorId);

    //Para buscar as builds por data de criação
    List<Build> findAllByOrderByCreatedAtAsc();
    List<Build> findAllByOrderByCreatedAtDesc();


    //Busca por quantidade de likes e dislikes
    // Likes
    @Query("""
        SELECT b FROM Build b
        LEFT JOIN b.ratings r
        GROUP BY b.id
        ORDER BY SUM(CASE WHEN r.rating = 1 THEN 1 ELSE 0 END) ASC
    """)
    List<Build> orderByLikesAsc();

    @Query("""
        SELECT b FROM Build b
        LEFT JOIN b.ratings r
        GROUP BY b.id
        ORDER BY SUM(CASE WHEN r.rating = 1 THEN 1 ELSE 0 END) DESC
    """)
    List<Build> orderByLikesDesc();

    // Dislikes
    @Query("""
        SELECT b FROM Build b
        LEFT JOIN b.ratings r
        GROUP BY b.id
        ORDER BY SUM(CASE WHEN r.rating = -1 THEN 1 ELSE 0 END) ASC
    """)
    List<Build> orderByDislikesAsc();

    @Query("""
        SELECT b FROM Build b
        LEFT JOIN b.ratings r
        GROUP BY b.id
        ORDER BY SUM(CASE WHEN r.rating = -1 THEN 1 ELSE 0 END) DESC
    """)
    List<Build> orderByDislikesDesc();


}
