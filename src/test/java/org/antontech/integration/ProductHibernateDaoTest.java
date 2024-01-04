package org.antontech.integration;

import org.antontech.model.Product;
import org.antontech.model.User;
import org.antontech.repository.IProductDao;
import org.antontech.repository.IUserDao;
import org.antontech.repository.ProductHibernateDao;
import org.antontech.repository.UserHibernateDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.sql.SQLException;
import java.util.List;
import static junit.framework.Assert.assertEquals;

public class ProductHibernateDaoTest {
    IProductDao productHibernateDao;
    IUserDao userHibernateDao;

    Product product;
    User user;

    @Before
    public void setup(){
        productHibernateDao = new ProductHibernateDao();
        userHibernateDao = new UserHibernateDao();
        product = new Product();

        user = new User();
        user.setTitle("test title");
        user.setEmail("test email");
        user.setPhone("test phone");
        user.setAddress("test address");
        user.setType("OEM");
        user.setIndustry("tes industry");
        user.setManagerName("test manager");
        user.setCompanyName("test company");
        userHibernateDao.save(user);


        product.setName("TestProduct");
        product.setDescription("Test description");
        product.setUser(user);
        productHibernateDao.save(product);
    }

    @After
    public void teardown(){
        userHibernateDao.delete(user.getUserId());
        productHibernateDao.delete(product.getId());
    }

    @Test
    public void getProductsTest() throws SQLException {
        List<User> userList = userHibernateDao.getUsers();
        assertEquals(3, userList.size());
    }

    @Test
    public void updateNameTest() {
        long productId = product.getId();
        String newName = "Updated Product Name";

        String originalProductName = product.getName();

        productHibernateDao.updateName(productId,newName);
        String updatedProductName = productHibernateDao.getById(productId).getName();

        assertEquals(newName, updatedProductName);

        productHibernateDao.updateName(productId, originalProductName);
    }

    @Test
    public void updateDescriptionTest() {
        long productId = product.getId();
        String newDescription = "Updated Product Description";

        String originalProductDesc = product.getDescription();

        productHibernateDao.updateDescription(productId,newDescription);
        String updatedProductDesc = productHibernateDao.getById(productId).getDescription();

        assertEquals(newDescription, updatedProductDesc);

        productHibernateDao.updateDescription(productId, originalProductDesc);
    }



}
