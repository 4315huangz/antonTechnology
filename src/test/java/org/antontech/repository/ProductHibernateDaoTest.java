package org.antontech.repository;

import org.antontech.model.Product;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import util.HibernateUtil;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductHibernateDaoTest {
    @Mock
    private SessionFactory mockSessionFactory;
    @Mock
    private Session mockSession;
    @Mock
    private Query mockQuery;
    @Mock
    private Transaction mockTransaction;

    @Test
    public void getProducts() {
        IProductDao productDao = new ProductHibernateDao();
        Product product = new Product(2,"Unit Test Product", "Unit Test Product");
        List<Product> res = List.of(product);

        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.list()).thenReturn(res);
            doNothing().when(mockSession).close();

            List<Product> actualRes = productDao.getProducts();
            assertEquals(res, actualRes);
        }
    }

    @Test
    public void getProductsTest_getHibernateException_throwHibernateException() {
        IProductDao productDao = new ProductHibernateDao();
        Product product = new Product(2,"Unit Test Product", "Unit Test Product");
        List<Product> res = List.of(product);
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.list()).thenReturn(res);
            doThrow(HibernateException.class).when(mockSession).close();
            assertThrows(HibernateException.class, () -> productDao.getProducts());
        }
    }

    @Test
    public void save_happyPass() {
        IProductDao productDao = new ProductHibernateDao();
        Product product = new Product(2,"Unit Test Product", "Unit Test Product");

        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);

            doNothing().when(mockTransaction).commit();
            doNothing().when(mockSession).close();
            boolean res = productDao.save(product);
            assertTrue(res);
        }
    }
}
