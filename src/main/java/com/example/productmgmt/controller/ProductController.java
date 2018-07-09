package com.example.productmgmt.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.productmgmt.model.Product;
import com.example.productmgmt.model.ProductDescription;
import com.example.productmgmt.security.JwtValidator;
import com.example.productmgmt.service.ProductDescriptionService;
import com.example.productmgmt.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	ProductDescriptionService productDescriptionService;

	@Autowired
	ProductService productService;

	@Autowired
	JwtValidator jwtValidator;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> readDetails(@PathVariable int id, @RequestHeader("Authorization") String accessToken)
			throws JSONException, NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {

		Product response = null;
		if (jwtValidator.verifyJWT(accessToken)) {
			response = productService.getProductDetails(id);
			if (response == null) {
				return new ResponseEntity<Product>(response, HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<Product>(response, HttpStatus.OK);
			}
		} else
			return new ResponseEntity<Product>(response, HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Product>> readAllDetails(@RequestHeader("Authorization") String accessToken)
			throws JSONException, NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
		List<Product> response = null;
		if (jwtValidator.verifyJWT(accessToken)) {
			response = productService.getAllProductDetails();
			if (response.isEmpty()) {
				return new ResponseEntity<List<Product>>(response, HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<List<Product>>(response, HttpStatus.OK);
			}
		} else
			return new ResponseEntity<List<Product>>(response, HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/descriptions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductDescription> create(@RequestBody ProductDescription productDescription,
			@RequestHeader("Authorization") String accessToken)
			throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, JSONException {
		ProductDescription response = null;
		if (jwtValidator.verifyJWT(accessToken)) {
			response = productDescriptionService.addProductDescription(productDescription);
			if (response == null) {
				return new ResponseEntity<ProductDescription>(response, HttpStatus.CONFLICT);
			} else {
				return new ResponseEntity<ProductDescription>(response, HttpStatus.CREATED);
			}
		} else
			return new ResponseEntity<ProductDescription>(response, HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/descriptions/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Optional<ProductDescription>> read(@PathVariable int id,
			@RequestHeader("Authorization") String accessToken)
			throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, JSONException {
		Optional<ProductDescription> response = null;
		if (jwtValidator.verifyJWT(accessToken)) {
			response = productDescriptionService.getProductDescription(id);
			if (response == null) {
				return new ResponseEntity<Optional<ProductDescription>>(response, HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<Optional<ProductDescription>>(response, HttpStatus.OK);
			}
		} else
			return new ResponseEntity<Optional<ProductDescription>>(response, HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/descriptions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductDescription>> readAll(@RequestHeader("Authorization") String accessToken)
			throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, JSONException {
		List<ProductDescription> response = null;
		if (jwtValidator.verifyJWT(accessToken)) {
			response = productDescriptionService.getAllProductDescription();
			if (response.isEmpty()) {
				return new ResponseEntity<List<ProductDescription>>(response, HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<List<ProductDescription>>(response, HttpStatus.OK);
			}
		} else
			return new ResponseEntity<List<ProductDescription>>(response, HttpStatus.UNAUTHORIZED);

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/descriptions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductDescription> update(@RequestBody ProductDescription productDescription,
			@RequestHeader("Authorization") String accessToken)
			throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, JSONException {
		ProductDescription response = null;
		if (jwtValidator.verifyJWT(accessToken)) {
			response = productDescriptionService.updateProductDescription(productDescription);
			if (response == null) {
				return new ResponseEntity<ProductDescription>(response, HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<ProductDescription>(response, HttpStatus.OK);
			}
		} else
			return new ResponseEntity<ProductDescription>(response, HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/descriptions/{id}")
	public ResponseEntity<String> delete(@PathVariable int id, @RequestHeader("Authorization") String accessToken)
			throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, JSONException {

		if (jwtValidator.verifyJWT(accessToken)) {
			String response = productDescriptionService.deleteProductDescription(id);
			if (response != null) {
				return new ResponseEntity<String>(response, HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<String>("No Product with the ID " + id, HttpStatus.NOT_FOUND);
			}
		} else
			return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);

	}

}
