package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;

@Component
public class AccountSqlDAO implements AccountDAO {

	private JdbcTemplate jdbcTemplate;

	public AccountSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Account getBalance(int userId) {
		Account account = new Account();

		String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

		while (results.next()) {
			account = mapRowToAccount(results);
		}

		return account;
	}

	@Override
	public void updateBalance(Account account){
		String sqlUpdateBalance = "UPDATE accounts SET balance = ? WHERE user_id = ?;";

		jdbcTemplate.update(sqlUpdateBalance, account.getBalance(), account.getUserId());
	}

	private Account mapRowToAccount(SqlRowSet results) {
		Account account = new Account();
		account.setId(results.getInt("account_id"));
		account.setUserId(results.getInt("user_id"));
		account.setBalance(results.getBigDecimal("balance"));
		return account;

	}

	@Override
	public String getUsernameFromAccountId(int accountId) {
		String sqlGetUsernameFromAccountID = "SELECT username FROM users JOIN accounts ON accounts.user_id = users.user_id WHERE account_id = ?;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetUsernameFromAccountID, accountId);
		
		String username = "";
		while(results.next()) {
			username = results.getString("username");
		}
		return username;
	}
}