package org.antontech.repository;

import org.antontech.model.Product;
import org.antontech.repository.Exception.ProductDaoException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
    @InjectMocks
    private ProductHibernateDao productDao;

    Product product = new Product(2, "Unit Test Product", "Unit Test Product");
    List<Product> res = List.of(product);

    @Test
    public void getProductsTest() {
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

    @Test(expected = ProductDaoException.class)
    public void getProductsTest_getUnexpectedException_throwUnexpectedException() {
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.list()).thenReturn(res);
            doThrow(ProductDaoException.class).when(mockSession).close();
            productDao.getProducts();
        }
    }

    @Test
    public void getByIdTest() {
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

    @Test(expected = ProductDaoException.class)
    public void getByIdTest_getProductDaoException_throwProductDaoException() {
        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
            when(mockQuery.uniqueResult()).thenReturn(product);
            doThrow(ProductDaoException.class).when(mockSession).close();
            productDao.getById(2);
        }
    }

    @Test
    public void saveProduct_happyPass() {
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            doNothing().when(mockTransaction).commit();
            doNothing().when(mockSession).close();
            assertTrue(productDao.save(product));
        }
    }

    @Test(expected = ProductDaoException.class)
    public void saveProduct_failTest() {
        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            doNothing().when(mockTransaction).commit();
            doThrow(ProductDaoException.class).when(mockSession).close();
            productDao.save(product);
        }

    }

    @Test
    public void updateNameTest() {
        long id = 2;
        String newName = "New name";

        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
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
    }

    @Test
    public void updateDescriptionTest() {
        long id = 2;
        String newDescription = "New description";
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
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
    }

    @Test
    public void deleteTest() {
        long id = 2;
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
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
    }

    @Test
    public void searchByDescription() {
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
            when(mockQuery.getResultList()).thenReturn(res);
            doNothing().when(mockSession).close();

            List<Product> actualRes = productDao.searchByDescription("Test");
            assertEquals(res, actualRes);
        }
    }
}
