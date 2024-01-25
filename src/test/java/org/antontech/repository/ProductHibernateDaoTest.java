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
import org.antontech.util.HibernateUtil;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    public void getProductsTest() {
        IProductDao productDao = new ProductHibernateDao();
        Product product = new Product(2, "Unit Test Product", "Unit Test Product");
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
        Product product = new Product(2, "Unit Test Product", "Unit Test Product");
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
    public void getByIdTest() {
        IProductDao productDao = new ProductHibernateDao();
        Product product = new Product(2, "Unit Test Product", "Unit Test Product");
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
            when(mockQuery.uniqueResult()).thenReturn(product);
            doNothing().when(mockSession).close();
            Product actualRes = productDao.getById(2);
            assertEquals(product, actualRes);
        }
    }

    @Test
    public void save_happyPass() {
        IProductDao productDao = new ProductHibernateDao();
        Product product = new Product(2, "Unit Test Product", "Unit Test Product");

        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            doNothing().when(mockTransaction).commit();
            doNothing().when(mockSession).close();
            boolean res = productDao.save(product);
            assertTrue(res);
        }
    }

    @Test
    public void updateNameTest() {
        IProductDao productDao = new ProductHibernateDao();
        long id = 2;
        String newName = "New name";

        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("id"), anyLong())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("name"), anyString())).thenReturn(mockQuery);
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
    }

    @Test
    public void updateDescriptionTest() {
        IProductDao productDao = new ProductHibernateDao();
        long id = 2;
        String newDescription = "New description";
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("id"), anyLong())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("description"), anyString())).thenReturn(mockQuery);
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
    }

    @Test
    public void deleteTest() {
        IProductDao productDao = new ProductHibernateDao();
        long id = 2;
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("id"), anyLong())).thenReturn(mockQuery);
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
    }

    @Test
    public void searchByDescription() {
        IProductDao productDao = new ProductHibernateDao();
        Product product = new Product(2, "Unit Test Product", "Unit Test Product");
        List<Product> res = List.of(product);
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("keyword"), anyString())).thenReturn(mockQuery);
            when(mockQuery.getResultList()).thenReturn(res);
            doNothing().when(mockSession).close();

            List<Product> actualRes = productDao.searchByDescription("Test");
            assertEquals(res, actualRes);
        }
    }
}
