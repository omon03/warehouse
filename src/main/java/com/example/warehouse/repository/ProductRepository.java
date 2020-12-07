package com.example.warehouse.repository;

import com.example.warehouse.models.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "product", path = "product")
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    Product findByName(@Param("name") String name);
    List<Product> findByShortName(@Param("shortName") String shortName);
    Optional<Product> findAllByShortName(@Param("shortName")  String shortName);
}
