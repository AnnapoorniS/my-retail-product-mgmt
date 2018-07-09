package com.example.productmgmt.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class JwtValidator {

	@Value("${spring.oauth.okta.issuerUrl}")
	String issuerUrl;
	@Value("${spring.oauth.okta.audience}")
	String audience;
	@Value("${spring.oauth.okta.jwksUri}")
	String jwksUri;
	Instant instant = Instant.now();
	long currentTime = instant.getEpochSecond();

	@Autowired
	RestTemplate restTemplate;

	public boolean verifyJWT(String jwt)
			throws JSONException, NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
		String token = jwt.split(" ")[1];
		String headersBase64 = token.split("\\.")[0];
		String payloadBase64 = token.split("\\.")[1];
		String signatureBase64 = token.split("\\.")[2];

		byte[] payloadBytes = Base64.getUrlDecoder().decode(payloadBase64.getBytes("UTF-8"));
		String payload = new String(payloadBytes);
		JSONObject payloadJson = new JSONObject(payload);

		if (payloadJson.getLong("exp") < currentTime) {
			System.out.println("Token expired");
			return false;
		}
		if (!payloadJson.getString("iss").matches(issuerUrl)) {
			System.out.println("Invalid Issuer");
			return false;
		}
		if (!payloadJson.getString("aud").matches(audience)) {
			System.out.println("Invalid Audience");
			return false;
		}

		String jwks = restTemplate.getForEntity(jwksUri, String.class).getBody();

		JSONObject jwksJson = new JSONObject(jwks);
		JSONArray keysJson = jwksJson.getJSONArray("keys");
		ArrayList<PublicKey> validKeys = new ArrayList<>();
		for (int i = 0; i < keysJson.length(); i++) {

			BigInteger modulus = new BigInteger(1,
					Base64.getUrlDecoder().decode(keysJson.optJSONObject(i).getString("n").getBytes()));
			BigInteger exponent = new BigInteger(1,
					Base64.getUrlDecoder().decode(keysJson.optJSONObject(i).getString("e").getBytes("UTF-8")));

			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			validKeys.add(keyFactory.generatePublic(publicKeySpec));
		}

		boolean verified = false;

		for (PublicKey publicKey : validKeys) {
			try {

				Signature signature = Signature.getInstance("SHA256withRSA");
				signature.initVerify(publicKey);
				signature.update((headersBase64 + "." + payloadBase64).getBytes("UTF-8"));
				verified = signature.verify(Base64.getUrlDecoder().decode(signatureBase64.getBytes()));
				if (verified) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
