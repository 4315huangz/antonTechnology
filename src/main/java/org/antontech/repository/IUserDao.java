package org.antontech.repository;

import org.antontech.model.Product;
import org.antontech.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDao {
    List<User> getUsers() throws SQLException;

    //create user
    boolean save(User user);

    //Search user by id
    User getById(int id);

    //Update company name
    void updateCompanyName(int id, String name);

    //Update address
    void updateAddress(int id, String address);

    //Update industry
    void updateIndustry(int id, String industry);

    //Update manager
    void updateManager(int id, String manager, String title, String email, String phone);

    //Delete user
    void delete(int id);

}
