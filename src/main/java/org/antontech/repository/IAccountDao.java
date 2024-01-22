package org.antontech.repository;

import org.antontech.model.Account;

public interface IAccountDao {
    Account getAccountByCredentials(String email, String password) throws Exception;

    Account getAccountById(Long id);
}
