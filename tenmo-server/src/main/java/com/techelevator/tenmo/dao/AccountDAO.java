package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {

	Account getBalance(int userId);
	
	void updateBalance(Account account);
	
	String getUsernameFromAccountId(int accountId);
}
