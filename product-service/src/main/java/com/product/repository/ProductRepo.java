package com.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.product.entity.Product;
import java.util.List;

@Repository
public interface ProductRepo extends MongoRepository<Product, String> {
    List<Product> findByCategory(String category);
}