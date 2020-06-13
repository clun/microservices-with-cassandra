package com.datastax.sample.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.datastax.sample.entity.Product;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends CassandraRepository<Product, UUID> {
    
    @Query("select * from products where id = ?0")
    Optional<Product> findByProductByIdO(UUID id);
    
}

