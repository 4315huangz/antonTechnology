package org.antontech.repository;

import org.antontech.model.Product;
import org.antontech.model.User;
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
        user.setManager_name("test manager");
        user.setCompany_name("test company");
        userHibernateDao.save(user);


        product.setName("TestProduct");
        product.setDescription("Test description");
        product.setUser(user);
        productHibernateDao.save(product);
    }

    @After
    public void teardown(){
        userHibernateDao.delete(user.getUser_id());
        productHibernateDao.delete(product.getId());
    }

    @Test
    public void getProductsTest() throws SQLException {
        List<Product> productList = productHibernateDao.getProducts();
        assertEquals(2, productList.size());
    }

    @Test
    public void updateNameTest() {
        // Define test values
        int productId = 1;
        String newName = "Updated Product Name";

        //retrieve original name
        Product product = productHibernateDao.getById(productId);
        String originalProductName = product.getName();

        //retrieve updated name
        productHibernateDao.updateName(productId,newName);
        String updatedProductName = productHibernateDao.getById(productId).getName();

        assertEquals(newName, updatedProductName);

        //reset the product name to its original name
        productHibernateDao.updateName(productId, originalProductName);



    }



}
