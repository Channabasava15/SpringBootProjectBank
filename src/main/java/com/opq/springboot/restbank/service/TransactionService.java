package com.opq.springboot.restbank.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opq.springboot.restbank.dao.TransactionRepository;
import com.opq.springboot.restbank.dao.UserRepository;
import com.opq.springboot.restbank.exception.InsufficientBalanceException;
import com.opq.springboot.restbank.exception.InvalidAmountException;
import com.opq.springboot.restbank.exception.ResourceNotFoundException;
import com.opq.springboot.restbank.model.DepositResponse;
import com.opq.springboot.restbank.model.Transaction;
import com.opq.springboot.restbank.model.TransactionType;
import com.opq.springboot.restbank.model.User;
import com.opq.springboot.restbank.model.WithdrawResponse;

import jakarta.transaction.Transactional;


@Service
public class TransactionService 
{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Transactional
	public DepositResponse deposit(String accountNumber, BigDecimal amount)
	{

		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidAmountException("Amount must be positive");
		}

		Optional<User> optionalUser = userRepository.findByAccountNumber(accountNumber);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			BigDecimal currentBalance = user.getBalance();
			user.setBalance(currentBalance.add(amount));
			userRepository.save(user);

			Transaction transaction = new Transaction();
			transaction.setUser(user);
			transaction.setTransactionType(TransactionType.DEPOSIT);
			transaction.setAmount(amount);
			transaction.setTimestamp(LocalDateTime.now());
			transaction.setCurrentBalance(user.getBalance());
			transactionRepository.save(transaction);

			return new DepositResponse(user.getBalance(), "Deposit successful");
		}
		else
		{
			throw new ResourceNotFoundException("User not found with account number: " + accountNumber);
		}

	}

	@Transactional
	public WithdrawResponse withdraw(String accountNumber, BigDecimal amount) 
	{
		if (amount.compareTo(BigDecimal.ZERO) <= 0) 
		{
			throw new InvalidAmountException("Withdrawal amount must be positive");
		}
		Optional<User> optionalUser = userRepository.findByAccountNumber(accountNumber);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			BigDecimal currentBalance = user.getBalance();
			if (currentBalance.compareTo(amount) < 0) {
				throw new InsufficientBalanceException("Insufficient balance");
			}
			user.setBalance(currentBalance.subtract(amount));
			userRepository.save(user);

			Transaction transaction = new Transaction();
			transaction.setUser(user);
			transaction.setTransactionType(TransactionType.WITHDRAWAL);
			transaction.setAmount(amount);
			transaction.setTimestamp(LocalDateTime.now());
			transaction.setCurrentBalance(user.getBalance());
			transactionRepository.save(transaction);

			return new WithdrawResponse(user.getBalance(), "Withdrawal successful");
		} else 
		{
			throw new ResourceNotFoundException("User not found with account number: " + accountNumber);
		}
	}
	
	
	public List<Transaction> getTransactionHistoryByAccountNumber(String accountNumber) 
    {
    	 Optional<User> optionalUser = userRepository.findByAccountNumber(accountNumber);
         if (optionalUser.isPresent()) {
             return transactionRepository.findTransactionsByAccountNumber(accountNumber);
         } else {
             throw new ResourceNotFoundException("User not found with account number: " + accountNumber);
         }
    }
	
	 public BigDecimal checkBalanceByAccountNumber(String accountNumber) {
	        Optional<User> optionalUser = userRepository.findByAccountNumber(accountNumber);
	        if (optionalUser.isPresent()) 
	        {
	        	User user = optionalUser.get();
	        	 return user.getBalance();
	         } else {
	             throw new ResourceNotFoundException("User not found with account number: " + accountNumber);
	         }
	        
	    }
}
