package org.antontech.repository;

import org.antontech.ApplicationBootstrap;
import org.antontech.model.Product;
import org.antontech.model.User;
import org.antontech.repository.Exception.ProductDaoException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class ProductHibernateDaoTest {
    @MockBean
    private SessionFactory mockSessionFactory;
    @Mock
    private Session mockSession;
    @Mock
    private Query mockQuery;
    @Mock
    private Transaction mockTransaction;
    @Autowired
    private ProductHibernateDao productDao;

    Product product = new Product(2, "Unit Test Product", "Unit Test Product", 10.6, "testUrl", new User());
    List<Product> res = List.of(product);

    @Test
    public void getProductsTest() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);            when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(res);
        doNothing().when(mockSession).close();

        List<Product> actualRes = productDao.getProducts();

        assertEquals(res, actualRes);
        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockQuery, times(1)).list();
        verify(mockSession, times(1)).close();
    }

    @Test
    public void getProductsTest_getUnexpectedException_throwUnexpectedException() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenThrow(HibernateException.class);

        ProductDaoException exception = assertThrows(ProductDaoException.class, () -> {
            productDao.getProducts();
        });

        assertEquals("Failed to get products due to unexpected error", exception.getMessage());
        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockSession, times(1)).close();
    }

    @Test
    public void getByIdTest() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);            when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(product);
        doNothing().when(mockSession).close();

        Product actualRes = productDao.getById(2);

        assertEquals(product, actualRes);
        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockQuery, times(1)).setParameter(anyString(), any());
        verify(mockQuery, times(1)).uniqueResult();
        verify(mockSession, times(1)).close();
    }


    @Test
    public void getByIdTest_getProductDaoException_throwProductDaoException() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);            when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenThrow(HibernateException.class);

        ProductDaoException exception = assertThrows(ProductDaoException.class, () -> {
            productDao.getById(2);
        });

        assertEquals("Failed to get product due to unexpected error", exception.getMessage());
        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockSession, times(1)).close();
    }

    @Test
    public void saveProduct_happyPass() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);            when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        assertTrue(productDao.save(product));

        verify(mockSession).save(product);
        verify(mockTransaction).commit();
        verify(mockSession).close();
    }


    @Test(expected = ProductDaoException.class)
    public void saveProduct_failTest() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);            when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        doNothing().when(mockTransaction).commit();
        doThrow(ProductDaoException.class).when(mockSession).close();
        productDao.save(product);
    }

    @Test
    public void updateNameTest() {
        long id = 2;
        String newName = "New name";

        when(mockSessionFactory.openSession()).thenReturn(mockSession);            when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        productDao.updateName(id, newName);
        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).beginTransaction();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockQuery, times(2)).setParameter(anyString(), any());
        verify(mockSession, times(1)).close();
        verify(mockTransaction, times(1)).commit();
        verify(mockQuery, times(1)).executeUpdate();

    }

    @Test
    public void updateDescriptionTest() {
        long id = 2;
        String newDescription = "New description";
        when(mockSessionFactory.openSession()).thenReturn(mockSession);            when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        productDao.updateDescription(id, newDescription);

        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).beginTransaction();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockQuery, times(2)).setParameter(anyString(), any());
        verify(mockSession, times(1)).close();
        verify(mockTransaction, times(1)).commit();
        verify(mockQuery, times(1)).executeUpdate();
    }

    @Test
    public void deleteTest() {
        long id = 2;
        when(mockSessionFactory.openSession()).thenReturn(mockSession);            when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        productDao.delete(id);

        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).beginTransaction();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockQuery, times(1)).setParameter(anyString(), any());
        verify(mockQuery, times(1)).executeUpdate();
        verify(mockSession, times(1)).close();
        verify(mockTransaction, times(1)).commit();

    }

    @Test
    public void searchByDescription() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);            when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(res);
        doNothing().when(mockSession).close();

        List<Product> actualRes = productDao.searchByDescription("Test");
        assertEquals(res, actualRes);
    }

    @Test
    public void getPictureUrlTest() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.get(eq(Product.class), anyLong())).thenReturn(product);
        doNothing().when(mockSession).close();

        String actualRes = productDao.getPictureUrl(product.getId());
        assertEquals(product.getPictureUrl(), actualRes);

    }

    @Test
    public void savePictureUrlTest() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);            when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        when(mockSession.get(eq(Product.class), anyLong())).thenReturn(product);
        doNothing().when(mockSession).update(eq(Product.class));
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        productDao.savePictureUrl(product.getId(), "NewUrl");

        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).beginTransaction();
        verify(mockSession, times(1)).get(eq(Product.class), anyLong());
        verify(mockSession, times(1)).update(product);
        verify(mockSession, times(1)).close();
        verify(mockTransaction, times(1)).commit();

    }
}
