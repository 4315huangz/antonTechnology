package org.antontech.integration;

import org.antontech.ApplicationBootstrap;
import org.antontech.model.User;
import org.antontech.repository.UserHibernateDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class UserHibernateDaoTest {
    @Autowired
    UserHibernateDao userHibernateDao;
    User user = new User();

    @Before
    public void setup() {
        user.setUserName("testUser");
        user.setPassword("12345678");
        user.setFirstName("Jack");
        user.setLastName("John");
        user.setEmail("test@emai.com");
        user.setCompanyName("ABC INC");
        user.setAddress("Milwaukee,Wisconsin");
        user.setIndustry("Auto");
        user.setTitle("Manager");
        user.setPhone("000-000-0000");
        user.setCompanyType("Supplier");
        userHibernateDao.save(user);
    }

    @After
    public void teardown() {
        userHibernateDao.delete(user.getUserId());
    }

    @Test
    public void getUsersTest() {
        List<User> userList = userHibernateDao.getUsers();
        assertEquals(5, userList.size());
    }

    @Test
    public void getByIdTest() {
        long userId = user.getUserId();
        User u = userHibernateDao.getById(userId);
        assertEquals(user.getUserName(), u.getUserName());
    }

    @Test
    public void getByIndustryTest() {
        List<User> us = userHibernateDao.getByIndustry("Auto");
        assertEquals(1, us.size());
    }

    @Test
    public void updateEmailTest(){
        long userId = user.getUserId();
        String newEmail = "update@email.com";
        String originalEmail = userHibernateDao.getById(userId).getEmail();
        userHibernateDao.updateEmail(userId, newEmail);
        String updatedEmail = userHibernateDao.getById(userId).getEmail();
        assertEquals(newEmail, updatedEmail);
    }

    @Test
    public void updatePasswordTest(){
        long userId = user.getUserId();
        String newPassword = "123";
        String oldPassword = userHibernateDao.getById(userId).getPassword();
        userHibernateDao.updatePassword(userId, newPassword);
        String updatedPassword = userHibernateDao.getById(userId).getPassword();
        assertEquals(newPassword, updatedPassword);
    }

    @Test
    public void updateCompanyTest() {
        long userId = user.getUserId();
        String newCompanyName = "Updated Company Name";
        String newAddress = "updated Address";
        String newIndustry = "Updated Industry";
        String oldCompanyName = userHibernateDao.getById(userId).getCompanyName();
        String oldAddress = userHibernateDao.getById(userId).getAddress();
        String oldIndustry = userHibernateDao.getById(userId).getIndustry();
        userHibernateDao.updateCompany(userId, newCompanyName, newAddress, newIndustry);
        User u = userHibernateDao.getById(userId);
        assertEquals(newCompanyName, u.getCompanyName());
        assertEquals(newAddress, u.getAddress());
        assertEquals(newIndustry, u.getIndustry());
    }

    @Test
    public  void updateManagerTest(){
        long userId = user.getUserId();
        String firstName = "updated FN";
        String lastName = "updated LN";
        String newTitle = "updated title";
        String newPhone = "updated phone";
        userHibernateDao.updateManager(userId, firstName, lastName, newTitle, newPhone);
        User newuser = userHibernateDao.getById(userId);

        assertEquals(firstName, newuser.getFirstName());
        assertEquals(lastName, newuser.getLastName());
        assertEquals(newTitle, newuser.getTitle());
        assertEquals(newPhone, newuser.getPhone());
    }
}

