package com.antonk.gymtracker.repository;

import com.antonk.gymtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUserId(UUID userId);

    Optional<User> findByUserLoginId(String userLoginId);

    void  deleteById(UUID userId);
}
