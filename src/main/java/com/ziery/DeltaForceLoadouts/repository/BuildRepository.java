package com.ziery.DeltaForceLoadouts.repository;

import com.ziery.DeltaForceLoadouts.entity.Build;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BuildRepository extends JpaRepository<Build, Long>, JpaSpecificationExecutor<Build> {
    Optional<Build> findByCode(String code);

    Page<Build> findByCreatorId(Long creatorId, Pageable pageable);

    //Page<Build> findByCreatorUsernameContainingIgnoreCase(String creatorName, Pageable pageable);



    //para buscar builds favortadas atraves do id do usuario
   @Query("""
    SELECT b FROM Build b 
    JOIN b.favoriteUsers u
    WHERE u.id = :userId
""")
   Page<Build> findFavoritesByUserId(Long userId, Pageable pageable);


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

    long countByCreatorId(Long id);



    //


}
