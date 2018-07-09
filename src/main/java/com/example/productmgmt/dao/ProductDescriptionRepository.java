package com.example.productmgmt.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.productmgmt.model.ProductDescription;

@Repository
public interface ProductDescriptionRepository extends MongoRepository<ProductDescription, Integer>{

}
