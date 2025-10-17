package com.ziery.DeltaForceLoadouts.security.repository;

import com.ziery.DeltaForceLoadouts.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
