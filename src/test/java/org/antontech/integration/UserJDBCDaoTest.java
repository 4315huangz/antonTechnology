package org.antontech.integration;

import org.antontech.model.Product;
import org.antontech.model.User;
import org.antontech.repository.UserJDBCDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UserJDBCDaoTest {
    UserJDBCDao userJDBCDao;
    User user;
    @Before
    public void setup(){
        userJDBCDao = new UserJDBCDao();
        user = new User();
        user.setCompanyName("Test Company");
        user.setAddress("Test address");
        user.setIndustry("Test Industry");
        user.setManagerName("Test Manager");
        user.setTitle("Test Title");
        user.setEmail("test@emai.com");
        user.setPhone("000-000-0000");
        user.setType("OEM");
        userJDBCDao.save(user);
    }

    @After
    public void teardown(){
        userJDBCDao.delete(user.getUserId());
    }

    @Test
    public void getUsers() {
        List<User> userList = userJDBCDao.getUsers();
        assertEquals(3, userList.size());
    }
}
