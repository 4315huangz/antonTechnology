package org.antontech.repository;

import org.antontech.model.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import static junit.framework.Assert.assertEquals;

public class ProductJDBCDaoTest {
    ProductJDBCDao productJDBCDao;
    @Before
    public void setup(){
        productJDBCDao = new ProductJDBCDao();
    }

    @After
    public void teardown(){
    }

    @Test
    public void getProductsTest() {
        List<Product> productList = productJDBCDao.getProducts();
        assertEquals(0, productList.size());
    }
}
