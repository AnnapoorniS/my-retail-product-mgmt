package com.example.productmgmt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.productmgmt.dao.ProductDescriptionRepository;
import com.example.productmgmt.model.ProductDescription;

@Service
public class ProductDescriptionService {

	@Autowired
	ProductDescriptionRepository productDescriptionRepository;

	public ProductDescription addProductDescription(ProductDescription productDescription) {
		ProductDescription response = null;
		if (productDescription != null) {
			if (!productDescriptionRepository.existsById(productDescription.getId())) {
				response = productDescriptionRepository.save(productDescription);
				System.out.println("New product with description added");
			} else {
				System.out.println("Product already exists");
			}
		}
		return response;
	}

	public ProductDescription updateProductDescription(ProductDescription productDescription) {
		ProductDescription response = null;
		if (productDescription != null) {
			if (productDescriptionRepository.existsById(productDescription.getId())) {
				response = productDescriptionRepository.save(productDescription);
				System.out.println("Product description updated");
			} else {
				System.out.println("Product does not exists");
			}
		}
		return response;
	}

	public Optional<ProductDescription> getProductDescription(int id) {

		if (productDescriptionRepository.existsById(id)) {
			return productDescriptionRepository.findById(id);
		} else {
			return null;
		}
	}

	public List<ProductDescription> getAllProductDescription() {
		return productDescriptionRepository.findAll();
	}

	public String deleteProductDescription(int id) {

		if (productDescriptionRepository.existsById(id)) {
			productDescriptionRepository.deleteById(id);
			System.out.println("Product with id " + id + " deleted");
			return "Product with id " + id + " deleted";
		} else {
			return null;
		}
	}
}
