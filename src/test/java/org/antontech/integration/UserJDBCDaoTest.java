package org.antontech.integration;

import org.antontech.model.User;
import org.antontech.repository.UserJDBCDao;
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
        userJDBCDao.save(user);
    }

    @After
    public void teardown(){
        userJDBCDao.delete(user.getUserId());
    }

    @Test
    public void getUsers() {
        List<User> userList = userJDBCDao.getUsers();
        assertEquals(5, userList.size());
    }
}
