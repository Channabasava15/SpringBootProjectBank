package com.opq.springboot.restbank.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.opq.springboot.restbank.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> 
{
	 @Query(value = "SELECT t.* FROM transaction t " +
	            "JOIN user u ON t.user_id = u.id " +
	            "WHERE u.account_number = :accountNumber", nativeQuery = true)
    List<Transaction> findTransactionsByAccountNumber(String accountNumber);
	
}


