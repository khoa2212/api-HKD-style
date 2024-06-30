package com.example.user_service.repository;

import com.example.user_service.entity.URLToken;
import com.example.user_service.entity.URLTokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface URLTokenRepository extends JpaRepository<URLToken, UUID> {
    @Query("SELECT t FROM URLToken t WHERE t.user.email = :email AND t.tokenType = :tokenType ORDER BY t.createdAt DESC")
    List<URLToken> findByEmailOrderByCreatedAt(
            @Param("email") String email,
            @Param("tokenType") URLTokenType tokenType
    );
}
