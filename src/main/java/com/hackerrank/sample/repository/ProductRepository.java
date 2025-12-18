package com.hackerrank.sample.repository;

import com.hackerrank.sample.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p FROM Product p " + "LEFT JOIN FETCH p.seller " + "WHERE p.id = :id")
    Optional<Product> findByIdWithDetails(@Param("id") Long id);
}
