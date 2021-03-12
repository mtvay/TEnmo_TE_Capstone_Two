package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.User;

public class UserService {

	private final String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	
	public UserService(String url) {
		this.BASE_URL = url;
	}
	
	public User[] getAll(String token) {
		User[] users = null;
		try {
			users = restTemplate.exchange(BASE_URL + "user", HttpMethod.GET, makeAuthEntity(token), User[].class).getBody();

		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode() + " " + e.getStatusText());

		} catch (ResourceAccessException e) {
			System.out.println(e.getMessage());
		}
		return users;
	}
	
	private HttpEntity makeAuthEntity(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
}
