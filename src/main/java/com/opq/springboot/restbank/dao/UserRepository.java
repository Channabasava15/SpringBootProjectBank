package com.opq.springboot.restbank.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.opq.springboot.restbank.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> 
{
    Optional<User> findByAccountNumber(String accountNumber);
}


