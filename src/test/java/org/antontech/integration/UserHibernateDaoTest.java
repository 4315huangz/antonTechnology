package org.antontech.integration;

import org.antontech.model.Product;
import org.antontech.model.User;
import org.antontech.repository.UserHibernateDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UserHibernateDaoTest {
    UserHibernateDao userHibernateDao;
    User user = new User();

    @Before
    public void setup() {
        userHibernateDao = new UserHibernateDao();
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
        //Define Test Values
        long userId = 3;
        String newCompanyName = "EDF.INC";
        String originalName = userHibernateDao.getById(userId).getCompanyName();
        userHibernateDao.updateCompanyName(userId, newCompanyName);
        String updatedName = userHibernateDao.getById(userId).getCompanyName();

        assertEquals(newCompanyName, updatedName);

        //reset the user's company name to its original name
        userHibernateDao.updateCompanyName(userId, originalName);
    }

    @Test
    public void updateAddressTest(){
        //Define Test Values
        long userId = 3;
        String newAddress = "CN";
        String originalAddress = userHibernateDao.getById(userId).getAddress();
        userHibernateDao.updateAddress(userId, newAddress);
        String updatedAddress = userHibernateDao.getById(userId).getAddress();

        assertEquals(newAddress, updatedAddress);

        //reset the user's company name to its original name
        userHibernateDao.updateAddress(userId, originalAddress);
    }

    @Test
    public void updateIndustryTest(){
        //Define Test Values
        long userId = 3;
        String newIndustry = "Grocery";
        String originalIndustry = userHibernateDao.getById(userId).getIndustry();
        userHibernateDao.updateIndustry(userId, newIndustry);
        String updatedIndustry = userHibernateDao.getById(userId).getIndustry();

        assertEquals(newIndustry, updatedIndustry);

        //reset the user's company name to its original name
        userHibernateDao.updateIndustry(userId, originalIndustry);
    }

    @Test
    public  void updateManagerTest(){
        long userId = 3;
        User user;
        //Get original parameters
        user = userHibernateDao.getById(userId);
        String originalManager = user.getManagerName();
        String originalTitle = user.getTitle();
        String originalEmail = user.getEmail();
        String originalPhone = user.getPhone();

        //Set new parameters
        String newManager = "testManager";
        String newTitle = "testTitle";
        String newEmail = "test@test.com";
        String newPhone = "testPhone";
        userHibernateDao.updateManager(userId, newManager, newTitle, newEmail, newPhone);

        //get new pamaneters
        user = userHibernateDao.getById(userId);
        String updatedManager = user.getManagerName();
        String updatedTitle = user.getTitle();
        String updatedEmail = user.getEmail();
        String updatedPhone = user.getPhone();

        assertEquals(newManager, updatedManager);
        assertEquals(newTitle, updatedTitle);
        assertEquals(newEmail, updatedEmail);
        assertEquals(newPhone, updatedPhone);

        userHibernateDao.updateManager(userId,originalManager,originalTitle,originalEmail,originalPhone );

    }

}
