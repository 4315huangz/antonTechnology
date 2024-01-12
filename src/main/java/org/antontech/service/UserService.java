package org.antontech.service;

import org.antontech.model.User;
import org.antontech.repository.IUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private IUserDao userDao;
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<User> getUsers(User user) {
        logger.info("Start to getUsers by UserService");
        if("OEM".equals(user.getType())) {
            return userDao.getSuppliers();
        } else if ("Supplier".equals((user.getType()))) {
            return userDao.getOEMs();
        }
        return List.of();
    }


    public List<User> getUsersByIndustry (User user, String industry) {
        logger.info("Start to getUsersByIndustry by UserService");
        List<User> users = getUsers(user);
        return users.stream()
                .filter(u -> userDao.getUsersByIndustry(industry).contains(u))
                .collect(Collectors.toList());
    }

    public boolean save(User user) {
        return userDao.save(user);
    }

    public void updateCompanyName(long id, String name) {
        userDao.updateCompanyName(id, name);
    }

    public void updateAddress(long id, String address) {
        userDao.updateAddress(id, address);
    }

    public void updateIndustry(long id, String industry) {
        userDao.updateIndustry(id, industry);
    }

    public void updateManager(long id, String manager, String title, String email, String phone) {
        userDao.updateManager(id, manager, title, email, phone);
    }

    public void delete(long id) {
        userDao.delete(id);
    }

}
