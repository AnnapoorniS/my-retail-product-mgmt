package com.example.productmgmt.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.productmgmt.dao.ProductDescriptionRepository;
import com.example.productmgmt.model.Price;
import com.example.productmgmt.model.Product;
import com.example.productmgmt.model.ProductDescription;

@Service
public class ProductService {

	@Autowired
	ProductDescriptionRepository productDescriptionRepository;

	@Autowired
	RestTemplate restTemplate;

	@Value("${spring.productPriceServer.host}")
	String productPriceServerHost;

	@Value("${spring.productPriceServer.path}")
	String productPriceServerPath;

	@Value("${spring.oauth.okta.tokenUrl}")
	String tokenUrl;

	@Value("${spring.oauth.okta.scope}")
	String scope;

	@Value("${spring.oauth.okta.grantType}")
	String grantType;

	@Value("${spring.oauth.okta.credentials}")
	String credentials;

	public Product getProductDetails(int id) throws JSONException {
		if (productDescriptionRepository.existsById(id)) {
			ProductDescription productDescription = productDescriptionRepository.findById(id).get();

			ResponseEntity<String> productPriceResponse;
			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept", "application/json");
			String accessToken = getAccessToken();
			headers.add("Authorization", "Bearer " + accessToken);
			HttpEntity<String> requestEntity = new HttpEntity<String>("parameters", headers);
			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromHttpUrl(productPriceServerHost + "/" + productPriceServerPath + "/" + id);
			try {
				productPriceResponse = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, requestEntity,
						String.class);
			} catch (HttpClientErrorException e) {
				e.printStackTrace();
				return null;
			}
			JSONObject productPriceJSON = new JSONObject(productPriceResponse.getBody());
			JSONObject priceJSON = productPriceJSON.getJSONObject("current_price");
			Price price = new Price(priceJSON.getDouble("value"), priceJSON.getString("currency_code"));
			Product product = new Product(id, productDescription.getDescription(), price);
			return product;

		} else
			return null;
	}

	public List<Product> getAllProductDetails() throws JSONException {

		List<ProductDescription> products = productDescriptionRepository.findAll();
		List<Product> response = new ArrayList<>();
		for (ProductDescription productDescription : products) {
			Product product = getProductDetails(productDescription.getId());
			response.add(product);
		}
		return response;
	}

	public String getAccessToken() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + credentials);
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		HttpEntity<String> requestEntity = new HttpEntity<String>("parameters", headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tokenUrl).queryParam("grant_type", grantType)
				.queryParam("scope", scope);
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, requestEntity,
				String.class);
		String response = result.getBody().toString();
		JSONObject responseJson = new JSONObject(response);
		return responseJson.getString("access_token");
	}

}
