package org.antontech.integration;

import org.antontech.ApplicationBootstrap;
import org.antontech.model.Product;
import org.antontech.model.User;
import org.antontech.repository.IProductDao;
import org.antontech.repository.IUserDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import static junit.framework.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class ProductHibernateDaoTest {
    @Autowired
    private IProductDao productHibernateDao;
    @Autowired
    private IUserDao userHibernateDao;
    Product product;
    User user;

    @Before
    public void setup(){
        user = new User();
        user.setUserName("testUser");
        user.setPassword("12345678");
        user.setFirstName("Jack");
        user.setLastName("John");
        user.setEmail("test@emai.com");
        user.setCompanyName("ABC INC");
        user.setAddress("Milwaukee,Wisconsin");
        user.setIndustry("Auto");
        user.setTitle("Manager");
        user.setPhone("000-000-0000");
        user.setCompanyType("Supplier");
        userHibernateDao.save(user);
        product = new Product();
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
    public void getProductsTest() {
        List<Product> products = productHibernateDao.getProducts();
        assertEquals(1, products.size());
    }

    @Test
    public void updateNameTest() {
        long productId = product.getId();
        String newName = "Updated Product Name";
        productHibernateDao.updateName(productId,newName);
        String updatedName = productHibernateDao.getById(productId).getName();
        assertEquals(newName, updatedName);
    }

    @Test
    public void updateDescriptionTest() {
        long productId = product.getId();
        String newDescription = "Updated Product Description";
        productHibernateDao.updateDescription(productId,newDescription);
        String updatedDesc = productHibernateDao.getById(productId).getDescription();

        assertEquals(newDescription, updatedDesc);
    }

    @Test
    public void searchByDescriptionKeywordTest() {
        List<Product> productList = productHibernateDao.searchByDescription("Test");
        assertEquals(1, productList.size());
    }
}
