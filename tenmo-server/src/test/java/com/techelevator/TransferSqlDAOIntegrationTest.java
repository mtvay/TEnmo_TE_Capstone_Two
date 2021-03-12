package com.techelevator;

import java.util.List;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.TransferSqlDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;

public class TransferSqlDAOIntegrationTest extends BaseDAOTests {
	
	private TransferDAO transferDao;
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
	private Transfer transfer1;
	private Transfer transfer2;
	private Transfer transfer3;
	
	@Before
	public void setup() {
		jdbcTemplate = new JdbcTemplate(dataSource);
		transferDao = new TransferSqlDAO(jdbcTemplate);
		
		String sqlTruncateUsers = "TRUNCATE TABLE users CASCADE";
		String sqlTruncateAccounts = "TRUNCATE TABLE accounts CASCADE;";
		String sqlTruncateTransfers = "TRUNCATE TABLE transfers CASCADE;";
		
		jdbcTemplate.update(sqlTruncateUsers);
		jdbcTemplate.update(sqlTruncateAccounts);
		jdbcTemplate.update(sqlTruncateTransfers);
		
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
		
		BigDecimal big50 = new BigDecimal(50);
		BigDecimal big75 = new BigDecimal(75);
		BigDecimal big100 = new BigDecimal(100);
		
		transfer1 = new Transfer(1, 2, 2, account1.getId(), account2.getId(), big50);
		transfer2 = new Transfer(2, 2, 2, account2.getId(), account3.getId(), big75);
		transfer3 = new Transfer(3, 2, 2, account3.getId(), account1.getId(), big100);
		
		String sqlAddUser = "INSERT INTO users (user_id, username, password_hash) VALUES (?, ?, ?);";
		String sqlAddAccount = "INSERT INTO accounts (account_id, user_id, balance) VALUES (?, ?, ?);";
		String sqlAddTransfer = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) "
				+ "VALUES (?, ?, ?, ?, ?, ?);";
		
		jdbcTemplate.update(sqlAddUser, user1.getId(), user1.getUsername(), user1.getPassword());
		jdbcTemplate.update(sqlAddUser, user2.getId(), user2.getUsername(), user2.getPassword());
		jdbcTemplate.update(sqlAddUser, user3.getId(), user3.getUsername(), user3.getPassword());
		jdbcTemplate.update(sqlAddAccount, account1.getId(), account1.getUserId(), account1.getBalance());
		jdbcTemplate.update(sqlAddAccount, account2.getId(), account2.getUserId(), account2.getBalance());
		jdbcTemplate.update(sqlAddAccount, account3.getId(), account3.getUserId(), account3.getBalance());
		jdbcTemplate.update(sqlAddTransfer, 1, 2, 2, account1.getId(), account2.getId(), big50);
		jdbcTemplate.update(sqlAddTransfer, 2, 2, 2, account2.getId(), account3.getId(), big75);
		jdbcTemplate.update(sqlAddTransfer, 3, 2, 2, account3.getId(), account1.getId(), big100);
	}
	
//	getTransferById
//	getAllTransfersByUserId
//	transfer

	@Test
	public void getTransferById_returns_the_correct_transfer() {
		//Arrange
		
		
		//Act
		Transfer result1 = transferDao.getTransferById(transfer1.getId());
		Transfer result2 = transferDao.getTransferById(transfer2.getId());
		Transfer result3 = transferDao.getTransferById(transfer3.getId());
		
		//Assert
		Assert.assertEquals(transfer1.getAccountFrom(), result1.getAccountFrom());
		Assert.assertEquals(transfer2.getAccountFrom(), result2.getAccountFrom());
		Assert.assertEquals(transfer3.getAccountFrom(), result3.getAccountFrom());
	}
	
	@Test
	public void getAllTransfersByUserId_returns_the_correct_transfers() {
		//Arrange
		final int TWO = 2;
		
		//Act
		List<Transfer> result1 = transferDao.getAllTransfersByUserId(10000);
		List<Transfer> result2 = transferDao.getAllTransfersByUserId(20000);
		List<Transfer> result3 = transferDao.getAllTransfersByUserId(30000);
		
		//Assert
		Assert.assertEquals(TWO, result1.size());
		Assert.assertEquals(TWO, result2.size());
		Assert.assertEquals(TWO, result3.size());
		
	}
	
	@Test
	public void transfer_correctly_adds_a_transfer_to_the_database() {
		//Arrange
		final int THREE = 3;
		BigDecimal big10 = new BigDecimal(10);
		Transfer newTransfer = new Transfer(4, 2, 2, account1.getId(), account2.getId(), big10);
		
		//Act
		transferDao.transfer(newTransfer);
		List<Transfer> result1 = transferDao.getAllTransfersByUserId(10000);
		List<Transfer> result2 = transferDao.getAllTransfersByUserId(20000);
		
		//Assert
		Assert.assertEquals(THREE, result1.size());
		Assert.assertEquals(THREE, result2.size());
	}
}
