package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;

@Component
public class TransferSqlDAO implements TransferDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	

    public TransferSqlDAO(JdbcTemplate jdbcTemplate) {
    	this.jdbcTemplate = jdbcTemplate;
    }
    
	@Override
	public Transfer getTransferById(int id) {
		String sqlGetAllTransfer = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount "
				+ "FROM transfers WHERE transfer_id = ?;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllTransfer, id);
		
		Transfer transfer = new Transfer();
		
		while (results.next()) {
			transfer = mapRowToTransfer(results);
		}
		return transfer;
	}

	@Override
	public List<Transfer> getAllTransfersByUserId(int id) {
		String sqlGetAllAccountFromTransfers = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount "
				+ "FROM transfers JOIN accounts ON account_from = account_id WHERE accounts.user_id = ?;";
		
		String sqlGetAllAccountToTransfers = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount "
				+ "FROM transfers JOIN accounts ON account_to = account_id WHERE accounts.user_id = ?;";
		
		SqlRowSet accountFromResults = jdbcTemplate.queryForRowSet(sqlGetAllAccountFromTransfers, id);
		SqlRowSet accountToResults = jdbcTemplate.queryForRowSet(sqlGetAllAccountToTransfers, id);
		
		List<Transfer> transfers = new ArrayList<>();
		
		while (accountFromResults.next()) {
			Transfer transfer = mapRowToTransfer(accountFromResults);
			transfers.add(transfer);
		}
		while (accountToResults.next()) {
			Transfer transfer = mapRowToTransfer(accountToResults);
			transfers.add(transfer);
		}
		
		return transfers;
	}

	
	@Override
	public Transfer transfer(Transfer transfer) {
		String sqlSaveTransferRecord = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) "
				+ "VALUES (2, 2, ?, ?, ?) RETURNING transfer_id;";
		
		int transferId = jdbcTemplate.queryForObject(sqlSaveTransferRecord, Integer.class, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
		
		transfer.setId(transferId);
		
		return transfer;
	}
	
    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getInt("transfer_id"));
        transfer.setTypeId(results.getInt("transfer_type_id"));
        transfer.setStatusId(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getInt("account_from"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
    
}

