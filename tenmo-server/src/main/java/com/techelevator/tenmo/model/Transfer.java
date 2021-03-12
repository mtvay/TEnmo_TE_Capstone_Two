package com.techelevator.tenmo.model;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class Transfer {

	private int id;
	
	@NotNull
	private int typeId;
	
	@NotNull
	private int statusId;
	
	@NotNull
	private int accountFrom;
	
	@NotNull
	private int accountTo;
	
	@NotNull
	@DecimalMin(value = "0.01", message = "Minimum transfer amount is 0.01 TE bucks.")
	private BigDecimal amount;

	public Transfer() {
	};

	public Transfer(int id, int typeId, int statusId, int from, int to, BigDecimal amount) {
		this.id = id;
		this.typeId = typeId;
		this.statusId = statusId;
		this.accountFrom = from;
		this.accountTo = to;
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(int from) {
		this.accountFrom = from;
	}

	public int getAccountTo() {
		return accountTo;
	}

	public void setAccountTo(int to) {
		this.accountTo = to;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTypeDesc() {
		if (typeId == 1) {
			return "Approve";
		} else
			return "Send";
	}

	public String getStatusDesc() {
		if (statusId == 1) {
			return "Pending";
		} else if (statusId == 2) {
			return "Approved";
		} else
			return "Rejected";
	}
}
