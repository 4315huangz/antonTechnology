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
    User getById(long id);

    //Search user by Industry
    List<User> getUsersByIndustry(String industry);

    //Update company name
    void updateCompanyName(long id, String name);

    //Update address
    void updateAddress(long id, String address);

    //Update industry
    void updateIndustry(long id, String industry);

    //Update manager
    void updateManager(long id, String manager, String title, String email, String phone);

    //Delete user
    void delete(long id);

}
