package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    public Account register(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Optional<Account> login(String username, String password) {
        Optional<Account> PotentialUser = accountRepository.findByUsername(username);
        if (PotentialUser.isPresent()) {
            Account user = PotentialUser.get();
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<Account> findAccountById(Integer accountId) {
        return accountRepository.findById(accountId);
    }


}
