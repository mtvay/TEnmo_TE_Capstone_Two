package com.techelevator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;
import com.techelevator.tenmo.model.User;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

public class UserSqlDAOIntegrationTest extends BaseDAOTests {
	
	private UserDAO userDao;
	private JdbcTemplate jdbcTemplate;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setup() {
		jdbcTemplate = new JdbcTemplate(dataSource);
		userDao = new UserSqlDAO(jdbcTemplate);
		
		String sqlTruncateUsers = "TRUNCATE TABLE users CASCADE";
		
		jdbcTemplate.update(sqlTruncateUsers);
		
		user1 = new User();
		user2 = new User();
		user3 = new User();
		
		user1.setUsername("One");
		user2.setUsername("Two");
		user3.setUsername("Three");
		user1.setId(10000L);
		user2.setId(20000L);
		user3.setId(30000L);
		user1.setPassword("blah");
		user2.setPassword("bleh");
		user3.setPassword("meh");
		
		String sqlAddUser = "INSERT INTO users (user_id, username, password_hash) VALUES (?, ?, ?);";
		
		jdbcTemplate.update(sqlAddUser, user1.getId(), user1.getUsername(), user1.getPassword());
		jdbcTemplate.update(sqlAddUser, user2.getId(), user2.getUsername(), user2.getPassword());
		jdbcTemplate.update(sqlAddUser, user3.getId(), user3.getUsername(), user3.getPassword());
		
	}
	
//	findIdByUsername
//	findAll
//	findByUsername
//	create
	
	@Test
	public void findIdByUsername_returns_the_correct_id() {
		//Arrange
		long oneResult = user1.getId();
		long twoResult = user2.getId();
		long threeResult = user3.getId();
				
		//Act
		long user1Result = userDao.findIdByUsername("ONE");
		long user2Result = userDao.findIdByUsername("Two");
		long user3Result = userDao.findIdByUsername("three");
		
		//Assert
		Assert.assertEquals(oneResult, user1Result);
		Assert.assertEquals(twoResult, user2Result);
		Assert.assertEquals(threeResult, user3Result);
		
	}

	@Test
	public void findAll_returns_all_users() {
		//Arrange
		List<User> expected = new ArrayList<>();
		expected.add(user1);
		expected.add(user2);
		expected.add(user3);
		
		//Act
		List<User> result = userDao.findAll();
		
		//Assert
		Assert.assertEquals(expected.size(), result.size());
		Assert.assertEquals(expected.get(1).getUsername(), result.get(1).getUsername());
	}
	
	@Test
	public void findByUsername_returns_the_correct_user() {
		//Arrange
		
		
		//Act
		User result1 = userDao.findByUsername("one");
		User result2 = userDao.findByUsername("TWO");
		User result3 = userDao.findByUsername("Three");
		
		//Assert
		Assert.assertEquals(user1.getUsername(), result1.getUsername());
		Assert.assertEquals(user1.getId(), result1.getId());
		Assert.assertEquals(user2.getUsername(), result2.getUsername());
		Assert.assertEquals(user2.getId(), result2.getId());
		Assert.assertEquals(user3.getUsername(), result3.getUsername());
		Assert.assertEquals(user3.getId(), result3.getId());
	}
	
	@Test
	public void create_returns_true_when_account_is_created() {
		//Arrange
		String username = "Testy McTestington";
		String password = "supersecurepassword";
		
		//Act
		boolean result = userDao.create(username, password);
		
		//Assert
		Assert.assertTrue("Create shoudl return true when successful", result);
	}
}
