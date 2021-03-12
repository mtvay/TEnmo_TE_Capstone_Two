package com.techelevator;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.AccountSqlDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import org.junit.Assert;

public class AccountSqlDAOIntegrationTest extends BaseDAOTests {
	
	private AccountDAO accountDao;
	private JdbcTemplate jdbcTemplate;
	private User user1;
	private User user2;
	private User user3;
	private Account account1;
	private Account account2;
	private Account account3;
	private BigDecimal balance500 = new BigDecimal(500.00);
	private BigDecimal balance600 = new BigDecimal(600.00);
	private BigDecimal balance700 = new BigDecimal(700.00);
	
	@Before
	public void setup() {
		jdbcTemplate = new JdbcTemplate(dataSource);
		accountDao = new AccountSqlDAO(jdbcTemplate);
		
		String sqlTruncateUsers = "TRUNCATE TABLE users CASCADE";
		String sqlTruncateAccounts = "TRUNCATE TABLE accounts CASCADE;";
		
		jdbcTemplate.update(sqlTruncateUsers);
		jdbcTemplate.update(sqlTruncateAccounts);
		
		user1 = new User();
		user2 = new User();
		user3 = new User();
		account1 = new Account();
		account2 = new Account();
		account3 = new Account();
		
		user1.setUsername("One");
		user2.setUsername("Two");
		user3.setUsername("Three");
		user1.setId(10000L);
		user2.setId(20000L);
		user3.setId(30000L);
		user1.setPassword("blah");
		user2.setPassword("bleh");
		user3.setPassword("meh");
		
		account1.setId(1);
		account1.setUserId(10000);
		account1.setBalance(balance500);
		account2.setId(2);
		account2.setUserId(20000);
		account2.setBalance(balance600);
		account3.setId(3);
		account3.setUserId(30000);
		account3.setBalance(balance700);
		
		String sqlAddUser = "INSERT INTO users (user_id, username, password_hash) VALUES (?, ?, ?);";
		String sqlAddAccount = "INSERT INTO accounts (account_id, user_id, balance) VALUES (?, ?, ?);";
		
		jdbcTemplate.update(sqlAddUser, user1.getId(), user1.getUsername(), user1.getPassword());
		jdbcTemplate.update(sqlAddUser, user2.getId(), user2.getUsername(), user2.getPassword());
		jdbcTemplate.update(sqlAddUser, user3.getId(), user3.getUsername(), user3.getPassword());
		jdbcTemplate.update(sqlAddAccount, account1.getId(), account1.getUserId(), account1.getBalance());
		jdbcTemplate.update(sqlAddAccount, account2.getId(), account2.getUserId(), account2.getBalance());
		jdbcTemplate.update(sqlAddAccount, account3.getId(), account3.getUserId(), account3.getBalance());
	}

//	getBalance
//	updateBalance
//	getUsernameFromAccountId
	
	@Test
	public void getBalance_returns_the_correct_balance() {
		//Arrange
		BigDecimal expected1 = account1.getBalance();
		BigDecimal expected2 = account2.getBalance();
		BigDecimal expected3 = account3.getBalance();
		
		//Act
		Account account1Result = accountDao.getBalance(account1.getUserId());
		Account account2Result = accountDao.getBalance(account2.getUserId());
		Account account3Result = accountDao.getBalance(account3.getUserId());
		
		//Assert
		Assert.assertEquals(0, expected1.compareTo(account1Result.getBalance()));
		Assert.assertEquals(0, expected2.compareTo(account2Result.getBalance()));
		Assert.assertEquals(0, expected3.compareTo(account3Result.getBalance()));
	}
	
	@Test
	public void updateBalance_correctly_updates_the_balance() {
		//Arrange
		BigDecimal big800 = new BigDecimal(800.00);
		BigDecimal big900 = new BigDecimal(900.00);
		BigDecimal big1000 = new BigDecimal(1000.00);
		account1.setBalance(big800);
		account2.setBalance(big900);
		account3.setBalance(big1000);
		
		//Act
		accountDao.updateBalance(account1);
		accountDao.updateBalance(account2);
		accountDao.updateBalance(account3);
		Account account1Result = accountDao.getBalance(account1.getUserId());
		Account account2Result = accountDao.getBalance(account2.getUserId());
		Account account3Result = accountDao.getBalance(account3.getUserId());
		
		//Assert
		Assert.assertEquals(0, big800.compareTo(account1Result.getBalance()));
		Assert.assertEquals(0, big900.compareTo(account2Result.getBalance()));
		Assert.assertEquals(0, big1000.compareTo(account3Result.getBalance()));
	}
	
	@Test
	public void getUsernameFromAccountId_returns_the_correct_username() {
		//Arrange
		String username1 = user1.getUsername();
		String username2 = user2.getUsername();
		String username3 = user3.getUsername();
		
		//Act
		String result1 = accountDao.getUsernameFromAccountId(account1.getId());
		String result2 = accountDao.getUsernameFromAccountId(account2.getId());
		String result3 = accountDao.getUsernameFromAccountId(account3.getId());
		
		//Assert
		Assert.assertEquals(username1, result1);
		Assert.assertEquals(username2, result2);
		Assert.assertEquals(username3, result3);
	}
}
