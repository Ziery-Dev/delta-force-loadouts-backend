package com.ziery.DeltaForceLoadouts.security.repository;

import com.ziery.DeltaForceLoadouts.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);


    //Implementação manual da deleção de uma build favoritada associada a um usuário (para que a build possa ser removida sem erro de chave estrageira)
    @Modifying
    @Query(value = "DELETE FROM user_favorites WHERE build_id = :buildId", nativeQuery = true)
    void deleteFavoritesByBuildId(@Param("buildId") Long buildId);
}
