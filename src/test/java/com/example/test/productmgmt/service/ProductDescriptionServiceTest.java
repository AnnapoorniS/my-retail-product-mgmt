package com.example.test.productmgmt.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.productmgmt.dao.ProductDescriptionRepository;
import com.example.productmgmt.model.ProductDescription;
import com.example.productmgmt.service.ProductDescriptionService;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { ProductDescriptionService.class })
public class ProductDescriptionServiceTest {

	@MockBean
	ProductDescriptionRepository productDescriptionRepository;
	
	@Autowired
	ProductDescriptionService productDescriptionService;
	@Test
	public void addProductDescriptionTest()
	{
		ProductDescription productDescription = new ProductDescription(1234, "testDesc");
		Mockito.when(productDescriptionRepository.save(productDescription)).thenReturn(productDescription);
		ProductDescription response = productDescriptionService.addProductDescription(productDescription);
		assert(response.getId()==productDescription.getId());
		assert(response.getDescription().matches(productDescription.getDescription()));	
	}
	@Test
	public void updateProductDescriptionTest()
	{
		ProductDescription productDescription = new ProductDescription(1234, "testDesc");
		Mockito.when(productDescriptionRepository.existsById(productDescription.getId())).thenReturn(true);
		Mockito.when(productDescriptionRepository.save(productDescription)).thenReturn(productDescription);
		ProductDescription response = productDescriptionService.updateProductDescription(productDescription);
		assert(response.getId()==productDescription.getId());
		assert(response.getDescription().matches(productDescription.getDescription()));	
	}
	@Test
	public void getAllProductDescriptionTest()
	{
		ProductDescription productDescription = new ProductDescription(1234, "testDesc");
		List<ProductDescription> list = new ArrayList<>();
		list.add(productDescription);
		Mockito.when(productDescriptionRepository.findAll()).thenReturn(list);
		List<ProductDescription> response = productDescriptionService.getAllProductDescription();
		assert(response.size()==1);
		assert(response.get(0).getId()==list.get(0).getId());
		assert(response.get(0).getDescription().matches(list.get(0).getDescription()));
	}
}
