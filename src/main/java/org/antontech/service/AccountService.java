package org.antontech.service;

import org.antontech.model.Account;
import org.antontech.repository.IAccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    IAccountDao accountDao;
    public Account getAccountByCredentials(String email, String password) throws Exception {
        return  accountDao.getAccountByCredentials(email, password);
    }

    public Account getAccountById(Long id) {
        return accountDao.getAccountById(id);
    }

}
