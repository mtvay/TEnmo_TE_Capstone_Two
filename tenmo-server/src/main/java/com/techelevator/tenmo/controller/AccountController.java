package com.techelevator.tenmo.controller;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.model.Account;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

	private AccountDAO accountDao;

	public AccountController(AccountDAO accountDao) {
		this.accountDao = accountDao;
	}

	@RequestMapping(path = "/user/{userId}/account", method = RequestMethod.GET)
	public Account getBalance(@PathVariable int userId) {
		return accountDao.getBalance(userId);
	}

	@RequestMapping(path = "/update", method = RequestMethod.PUT)
	public void updateBalance(@Valid @RequestBody Account account){
		accountDao.updateBalance(account);
	}
	
	@RequestMapping(path = "/username/{accountId}", method = RequestMethod.GET)
		public String getUsernameFromAccountId(@PathVariable int accountId) {
		return accountDao.getUsernameFromAccountId(accountId);
	}
}
