package org.antontech.integration;

import org.antontech.ApplicationBootstrap;
import org.antontech.model.Product;
import org.antontech.model.User;
import org.antontech.repository.UserHibernateDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
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
        user.setCompanyName("ABC.INC");
        user.setAddress("US");
        user.setIndustry("Electricity");
        user.setManagerName("John Jay");
        user.setTitle("President");
        user.setEmail("John.Jay@edf.com");
        user.setPhone("123-456-7890");
        user.setType("OEM");
        userHibernateDao.save(user);

    }

    @After
    public void teardown() {
        userHibernateDao.delete(user.getUserId());
    }

    @Test
    public void getUsersTest() throws SQLException {
        List<User> userList = userHibernateDao.getUsers();
        assertEquals(3, userList.size());

    }

    @Test
    public void updateCompanyNameTest(){
        long userId = user.getUserId();
        String newCompanyName = "Updated Company Name";
        String originalName = userHibernateDao.getById(userId).getCompanyName();
        userHibernateDao.updateCompanyName(userId, newCompanyName);
        String updatedName = userHibernateDao.getById(userId).getCompanyName();
        assertEquals(newCompanyName, updatedName);

        userHibernateDao.updateCompanyName(userId, originalName);
    }

    @Test
    public void updateAddressTest(){
        long userId = user.getUserId();
        String newAddress = "Updated Address";
        String originalAddress = userHibernateDao.getById(userId).getAddress();
        userHibernateDao.updateAddress(userId, newAddress);
        String updatedAddress = userHibernateDao.getById(userId).getAddress();

        assertEquals(newAddress, updatedAddress);

        userHibernateDao.updateAddress(userId, originalAddress);
    }

    @Test
    public void updateIndustryTest(){
        long userId = user.getUserId();
        String newIndustry = "Grocery";
        String originalIndustry = userHibernateDao.getById(userId).getIndustry();
        userHibernateDao.updateIndustry(userId, newIndustry);
        String updatedIndustry = userHibernateDao.getById(userId).getIndustry();

        assertEquals(newIndustry, updatedIndustry);

        userHibernateDao.updateIndustry(userId, originalIndustry);
    }

    @Test
    public  void updateManagerTest(){
        long userId = user.getUserId();
        String originalManager = user.getManagerName();
        String originalTitle = user.getTitle();
        String originalEmail = user.getEmail();
        String originalPhone = user.getPhone();

        String newManager = "updated manager";
        String newTitle = "updated title";
        String newEmail = "updateemail@test.com";
        String newPhone = "updated phone";
        userHibernateDao.updateManager(userId, newManager, newTitle, newEmail, newPhone);

        User newuser = userHibernateDao.getById(userId);
        String updatedManager = newuser.getManagerName();
        String updatedTitle = newuser.getTitle();
        String updatedEmail = newuser.getEmail();
        String updatedPhone = newuser.getPhone();

        assertEquals(newManager, updatedManager);
        assertEquals(newTitle, updatedTitle);
        assertEquals(newEmail, updatedEmail);
        assertEquals(newPhone, updatedPhone);

        userHibernateDao.updateManager(userId,originalManager,originalTitle,originalEmail,originalPhone );

    }

}
