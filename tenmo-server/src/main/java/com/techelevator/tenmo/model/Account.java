package com.techelevator.tenmo.model;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class Account {

	private int id;
	
	@NotNull
	private int userId;
	
	@NotNull
	@DecimalMin(value = "0.00", message = "Account balance cannot be below 0.00 TE bucks.")
	private BigDecimal balance;
	
	public Account () {}
	
	public Account(int id, int userId, BigDecimal balance) {
		this.id = id;
		this.userId = userId;
		this.balance = balance;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
}
