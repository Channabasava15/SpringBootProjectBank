package com.opq.springboot.restbank.controller;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opq.springboot.restbank.model.DepositResponse;
import com.opq.springboot.restbank.model.Transaction;
import com.opq.springboot.restbank.model.WithdrawResponse;
import com.opq.springboot.restbank.service.TransactionService;



@RestController
@RequestMapping("/api/user")
public class TransationController 
{

	@Autowired
    private TransactionService transactionService;
	
	    @PutMapping("/deposit/{accountNumber}")
	    public ResponseEntity<DepositResponse> deposit(@PathVariable String accountNumber,@RequestParam BigDecimal amount) {
	        DepositResponse response = transactionService.deposit(accountNumber, amount);
	        return ResponseEntity.ok(response);
	    }

	    @PutMapping("/withdraw/{accountNumber}")
	    public ResponseEntity<WithdrawResponse> withdraw(@PathVariable String accountNumber,@RequestParam BigDecimal amount) {
	        WithdrawResponse response = transactionService.withdraw(accountNumber, amount);
	        return ResponseEntity.ok(response);
	    }

	    @GetMapping("/transaction-history/{accountNumber}")
	    public List<Transaction> getTransactionHistory(@PathVariable String accountNumber) {
	        return transactionService.getTransactionHistoryByAccountNumber(accountNumber);
	    }
	  
	    @GetMapping("/balance/{accountNumber}")
	    public ResponseEntity<String> checkBalance(@PathVariable String accountNumber) {
	        BigDecimal balance = transactionService.checkBalanceByAccountNumber(accountNumber);
	        String responseMessage = "Available Balance: " + balance.toString();
	        return ResponseEntity.ok(responseMessage);
	    }
}
