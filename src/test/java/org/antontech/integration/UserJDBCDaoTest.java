package org.antontech.integration;

import junit.framework.Assert;
import org.antontech.model.User;
import org.antontech.repository.UserJDBCDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UserJDBCDaoTest {
    UserJDBCDao userJDBCDao;
    User user;
    @Before
    public void setup(){
        userJDBCDao = new UserJDBCDao();
        user = new User();
        user.setUserName("John_20");
        user.setPassword("12345678");
        user.setFirstName("Jack");
        user.setLastName("John");
        user.setEmail("john.jake@gmail.com");
        user.setCompanyName("ABC INC");
        user.setAddress("Milwaukee, Wisconsin");
        user.setIndustry("Auto");
        user.setTitle("Manager");
        user.setPhone("000-000-0000");
        user.setCompanyType("Supplier");
        userJDBCDao.save(user);
    }

    @After
    public void teardown(){
        userJDBCDao.delete(user.getUserId());
    }

    @Test
    public void getUsers() {
        List<User> userList = userJDBCDao.getUsers();
        assertEquals(8, userList.size());
    }

    @Test
    public void getByIdTest() {
        User u = userJDBCDao.getById(9);
        assertEquals(9, u.getUserId());
    }

    @Test
    public void getByIndustryTest() {
        int actual = userJDBCDao.getByIndustry("Auto").size();
        assertEquals(1, actual);
    }

    @Test
    public void updateEmailTest() {
        String newEmail = "updateemail@test.com";
        long userId = user.getUserId();
        userJDBCDao.updateEmail(userId, newEmail);
        String actual = userJDBCDao.getById(userId).getEmail();
        assertEquals(newEmail, actual);
    }

    @Test
    public void updatePasswordTest() {
        String newPassword = "66666";
        String newHashedPassword = DigestUtils.md5Hex(newPassword.trim());
        userJDBCDao.updatePassword(9, newPassword);
        User u = userJDBCDao.getById(9);
        String hashedHashedPw = DigestUtils.md5Hex(newHashedPassword);
        assertEquals(hashedHashedPw, u.getPassword());
    }

    @Test
    public void updateComapnyTest() {
        String newCompany = "Google";
        String newAddress = "US";
        String newIndustry = "IT";
        userJDBCDao.updateCompany(9, newCompany, newAddress, newIndustry);
        User u = userJDBCDao.getById(9);
        assertEquals(newCompany, u.getCompanyName());
        assertEquals(newAddress, u.getAddress());
        assertEquals(newIndustry, u.getIndustry());
    }

    @Test
    public void updateManagerTest() {
        String newFN = "Michelle";
        String newLN = "Mayer";
        String newTitle = "Senior Manager";
        String newPhone = "987-777-7777";
        userJDBCDao.updateManager(9, newFN, newLN, newTitle, newPhone);
        User u = userJDBCDao.getById(9);
        assertEquals(newFN, u.getFirstName());
        assertEquals(newLN, u.getLastName());
        assertEquals(newTitle, u.getTitle());
        assertEquals(newPhone, u.getPhone());
    }
}
