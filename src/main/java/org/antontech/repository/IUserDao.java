package org.antontech.repository;

import org.antontech.model.User;
import java.util.List;

public interface IUserDao {
    List<User> getUsers();

    boolean save(User user);

    User getById(long id);

    List<User> getByIndustry(String industry);

    void updateEmail(long id, String email);

    void updatePassword(long id, String password);

    void updateCompany(long id, String companyName, String address, String industry);

    void updateManager(long id, String firstName, String lastName, String title, String phone);

    void delete(long id);

    User getUserByCredentials(String email, String password) throws Exception;

}
