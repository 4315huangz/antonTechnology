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
        user.setUserName("zjiang");
        user.setPassword("12345678");
        user.setFirstName("Zhi");
        user.setLastName("Jiang");
        user.setEmail("jiang@gmail.com");
        user.setCompanyName("Marquette University");
        user.setAddress("Milwaukee, Wisconsin");
        user.setIndustry("Auto");
        user.setTitle("Associate");
        user.setPhone("888-777-66666");
        user.setCompanyType("OEM");
        userHibernateDao.save(user);
        product = new Product();
        product.setName("Seating");
        product.setDescription("The auto seating supplier");
        product.setPrice(10.6);
        product.setUser(user);
        product.setPictureUrl("Test Url");
        productHibernateDao.save(product);
    }

    @After
    public void teardown(){
        productHibernateDao.delete(product.getId());
        userHibernateDao.delete(user.getUserId());

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

    @Test
    public void getPictureUrlTest() {
        String expectedUrl = product.getPictureUrl();
        assertEquals(expectedUrl, productHibernateDao.getPictureUrl(product.getId()));
    }

    @Test
    public void savePictureUrlTest() {
        String expectedUrl = "New Test Url";
        productHibernateDao.savePictureUrl(product.getId(), expectedUrl);
        String actual = productHibernateDao.getPictureUrl(product.getId());
        assertEquals(expectedUrl, actual);
    }
}
