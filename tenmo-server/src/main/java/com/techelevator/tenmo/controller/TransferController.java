package com.techelevator.tenmo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

	private TransferDAO transferDao;

	public TransferController(TransferDAO transferDao) {
		this.transferDao = transferDao;
	}

	@RequestMapping(path = "/user/{userId}/transfer", method = RequestMethod.GET)
	public List<Transfer> list(@PathVariable int userId) {

		return transferDao.getAllTransfersByUserId(userId);
	}

	@RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.GET)
	public Transfer getTransfer(@PathVariable int transferId) {

		return transferDao.getTransferById(transferId);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "/transfer", method = RequestMethod.POST)
	public Transfer saveTransfer(@Valid @RequestBody Transfer transfer) {
		return transferDao.transfer(transfer);
	}
}
