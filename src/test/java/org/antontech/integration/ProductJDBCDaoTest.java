package org.antontech.integration;

import org.antontech.model.Product;
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
        product.setPrice(10.6);
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
    public void getByIdTest() {
        Product p = productJDBCDao.getById(1);
        assertEquals(1, p.getId());
    }

    @Test
    public void updateNameTest() {
        String newName = "New name";
        long productIdToBeUpdated = product.getId();
        productJDBCDao.updateName(productIdToBeUpdated, newName);
        String actual = productJDBCDao.getById(productIdToBeUpdated).getName();
        assertEquals(newName, actual);
    }

    @Test
    public void updateDescriptionTest() {
        String newDes = "New description";
        long productIdToBeUpdated = product.getId();
        productJDBCDao.updateDescription(productIdToBeUpdated, newDes);
        String actual = productJDBCDao.getById(productIdToBeUpdated).getDescription();
        assertEquals(newDes, actual);
    }

    @Test
    public void searchByDescriptionKeywordTest() {
        List<Product> productList = productJDBCDao.searchByDescription("Tested");
        assertEquals(1, productList.size());
    }

}
