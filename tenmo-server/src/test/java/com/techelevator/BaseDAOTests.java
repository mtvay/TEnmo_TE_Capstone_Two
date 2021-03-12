package com.techelevator;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class BaseDAOTests {
	
	 static SingleConnectionDataSource dataSource;

	    @BeforeClass
	    public static void setupDataSource() {
	        dataSource = new SingleConnectionDataSource();
	        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
	        dataSource.setUsername("postgres");
	        dataSource.setPassword("postgres1");
	        dataSource.setAutoCommit(false);
	    }

	    @AfterClass
	    public static void closeDataSource() throws SQLException {
	        dataSource.destroy();
	    }

	    @After
	    public void rollback() throws SQLException {
	        dataSource.getConnection().rollback();
	    }
}
