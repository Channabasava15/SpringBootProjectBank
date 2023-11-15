package com.opq.springboot.restbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opq.springboot.restbank.dao.UserRepository;
import com.opq.springboot.restbank.exception.DuplicateAccountException;
import com.opq.springboot.restbank.exception.InvalidLoginException;
import com.opq.springboot.restbank.exception.NonZeroBalanceException;
import com.opq.springboot.restbank.exception.ResourceNotFoundException;
import com.opq.springboot.restbank.model.User;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

//import javax.transaction.Transactional;


@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public User createUser(User user) 
	{
		if (userRepository.findByAccountNumber(user.getAccountNumber()).isPresent()) {
			throw new DuplicateAccountException("Account number already exists");
		}
		return userRepository.save(user);
	}
	public User login(String accountNumber, String password) {
		Optional<User> optionalUser = userRepository.findByAccountNumber(accountNumber);
		if (optionalUser.isPresent()) 
		{
			User user = optionalUser.get();
			if (user.getPassword().equals(password)) {
				return user;
			} else {
				throw new InvalidLoginException("Invalid password");
			}
		} else {
			throw new ResourceNotFoundException("User not found with account number: " + accountNumber);
		}
	}
	public User getUserByAccountNumber(String accountNumber) {
		return userRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with account number: " + accountNumber));
	}

	@Transactional
    public String deleteAccount(String accountNumber) {
        Optional<User> optionalUser = userRepository.findByAccountNumber(accountNumber);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            BigDecimal balance = user.getBalance();

            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                throw new NonZeroBalanceException("Account balance must be zero before deletion. Current balance: " + balance);
            } else {
                userRepository.delete(user);
                return "Account successfully deleted.";
            }
        } else {
            throw new ResourceNotFoundException("User not found with account number: " + accountNumber);
        }
    }
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

//create car loan
	//test line
 

 // add loan features
}


