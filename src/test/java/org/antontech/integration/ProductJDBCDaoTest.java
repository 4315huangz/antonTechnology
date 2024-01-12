package org.antontech.integration;

import org.antontech.model.Product;
import org.antontech.repository.ProductJDBCDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import static junit.framework.Assert.assertEquals;

public class ProductJDBCDaoTest {
    ProductJDBCDao productJDBCDao;
    Product product;
    @Before
    public void setup(){
        productJDBCDao = new ProductJDBCDao();
        product = new Product();
        product.setName("Test Product");
        product.setDescription("Test product's description.");
        productJDBCDao.save(product);
    }

    @After
    public void teardown(){
        productJDBCDao.delete(product.getId());
    }

    @Test
    public void getProductsTest() {
        List<Product> productList = productJDBCDao.getProducts();
        assertEquals(2, productList.size());
    }

    @Test
    public void searchByDescriptionKeywordTest() {
        List<Product> productList = productJDBCDao.searchByDescriptionKeyword("Test");
        assertEquals(2, productList.size());

    }

}
