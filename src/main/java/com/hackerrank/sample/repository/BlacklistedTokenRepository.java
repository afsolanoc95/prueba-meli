package com.hackerrank.sample.repository;

import com.hackerrank.sample.model.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    Boolean existsByToken(String token);
}
