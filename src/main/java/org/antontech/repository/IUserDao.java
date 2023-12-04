package org.antontech.repository;

import org.antontech.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDao {
    List<User> getUsers() throws SQLException;

}
