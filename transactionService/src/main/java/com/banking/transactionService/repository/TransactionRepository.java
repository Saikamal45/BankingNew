package com.banking.transactionService.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.banking.transactionService.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>{

	List<Transaction> findTransactionsByAccountId(int accountId);

	
	List<Transaction> findByAccountIdAndTransactionCreatedAtBetween(int accountId, LocalDateTime start, LocalDateTime end);

}
