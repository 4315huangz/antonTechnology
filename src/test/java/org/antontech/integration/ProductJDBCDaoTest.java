package org.antontech.integration;

import org.antontech.model.Product;
import org.antontech.model.User;
import org.antontech.repository.ProductJDBCDao;
import org.antontech.repository.UserJDBCDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import static junit.framework.Assert.assertEquals;

public class ProductJDBCDaoTest {
    ProductJDBCDao productJDBCDao;
    UserJDBCDao userJDBCDao;
    Product product;

    @Before
    public void setup(){
        productJDBCDao = new ProductJDBCDao();
        userJDBCDao = new UserJDBCDao();
        product = new Product();
        product.setName("Test Product");
        product.setDescription("Test product's description.");
        product.setUser(userJDBCDao.getById(4) );
        productJDBCDao.save(product);
    }

    @After
    public void teardown(){
        productJDBCDao.delete(product.getId());
    }

    @Test
    public void getProductsTest() {
        List<Product> productList = productJDBCDao.getProducts();
        assertEquals(1, productList.size());
    }

    @Test
    public void searchByDescriptionKeywordTest() {
        List<Product> productList = productJDBCDao.searchByDescription("Test");
        assertEquals(1, productList.size());

    }

}
