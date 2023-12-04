package org.antontech.repository;

import org.antontech.model.Product;
import org.antontech.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UserJDBCDaoTest {
    UserJDBCDao userJDBCDao;
    @Before
    public void setup(){
        userJDBCDao = new UserJDBCDao();
    }

    @After
    public void teardown(){
    }

    @Test
    public void getUsers() {
        List<User> userList = userJDBCDao.getUsers();
        assertEquals(0, userList.size());
    }
}
