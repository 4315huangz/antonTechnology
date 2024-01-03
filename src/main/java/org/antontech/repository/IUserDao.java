package org.antontech.repository;

import org.antontech.model.Product;
import org.antontech.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDao {
    List<User> getUsers() throws SQLException;

    boolean save(User user);

    User getById(long id);

    List<User> getUsersByIndustry(String industry);

    void updateCompanyName(long id, String name);

    void updateAddress(long id, String address);

    void updateIndustry(long id, String industry);

    void updateManager(long id, String manager, String title, String email, String phone);

    void delete(long id);

}
