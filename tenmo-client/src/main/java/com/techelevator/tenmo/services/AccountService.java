package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;

public class AccountService {
	
	private final String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();

	public AccountService(String url) {
		this.BASE_URL = url;
	}

	public Account getBalance(int userId, String token) {

		Account account = new Account();
		try {
			account = restTemplate
					.exchange(BASE_URL + "user/" + userId + "/account", HttpMethod.GET, makeAuthEntity(token), Account.class)
					.getBody();
		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode() + " " + e.getStatusText());

		} catch (ResourceAccessException e) {
			System.out.println(e.getMessage());
		}
		return account;

	}

	public void update(Account account, String token) {
		try {
			restTemplate.exchange(BASE_URL + "update", HttpMethod.PUT, makeAccountEntity(account, token), Account.class).getBody();
		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode() + " " + e.getStatusText());

		} catch (ResourceAccessException e) {
			System.out.println(e.getMessage());
		}
	}

	public String getUsernameFromAccountId(int accountId, String token) {
		String username = "";
		try {
			username = restTemplate.exchange(BASE_URL + "username/" + accountId, HttpMethod.GET, makeAuthEntity(token), String.class).getBody();
		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode() + " " + e.getStatusText());

		} catch (ResourceAccessException e) {
			System.out.println(e.getMessage());
		}
		return username;
	}
	
	 private HttpEntity<Account> makeAccountEntity(Account account, String token) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setBearerAuth(token);
	        HttpEntity<Account> entity = new HttpEntity<>(account, headers);
	        return entity;
	      }

	private HttpEntity makeAuthEntity(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
}