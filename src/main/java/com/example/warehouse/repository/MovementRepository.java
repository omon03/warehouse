package com.example.warehouse.repository;

import com.example.warehouse.models.Product;
import com.example.warehouse.models.ProductMovement;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface MovementRepository extends PagingAndSortingRepository<ProductMovement, Long> {

    Iterable<ProductMovement> findAllByProduct(@Param("product") Product product);
}
