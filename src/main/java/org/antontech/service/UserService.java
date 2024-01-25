package org.antontech.service;

import org.antontech.model.User;
import org.antontech.repository.IUserDao;
import org.antontech.repository.UserHibernateDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserHibernateDao userDao;
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public boolean save(User user) {
        return userDao.save(user);
    }

    public User getById(long id) { return userDao.getById(id); }

    public List<User> getByIndustry(String industry) {
        return userDao.getByIndustry(industry);
    }

    public void updateEmail(long id, String email) {
        userDao.updateEmail(id, email);
    }

    public void updatePassword(long id, String password) {
        userDao.updatePassword(id, password);
    }

    public void updateCompany(long id, String companyName, String address, String industry) { userDao.updateCompany(id, companyName, address, industry); }

    public void updateManager(long id, String firstName, String lastName, String title, String phone) {
        userDao.updateManager(id, firstName, lastName, title, phone);
    }

    public void delete(long id) {
        userDao.delete(id);
    }

    public User getUserByCredentials(String email, String password) throws Exception {
        return  userDao.getUserByCredentials(email, password);
    }

}
