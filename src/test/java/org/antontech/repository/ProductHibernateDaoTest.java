package org.antontech.repository;

import org.antontech.model.Product;
import org.antontech.model.User;
import org.antontech.repository.Exception.ProductDaoException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

    Product product = new Product(2, "Unit Test Product", "Unit Test Product", 10.6, "testUrl", new User());
    List<Product> res = List.of(product);


    @Before
    public void setup() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
    }

    @Test
    public void getProductsTest() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(res);
        doNothing().when(mockSession).close();

        List<Product> actualRes = productDao.getProducts();

        assertEquals(res, actualRes);
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).list();
        verify(mockSession).close();
    }

    @Test
    public void getProductsTest_getUnexpectedException_throwUnexpectedException() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenThrow(HibernateException.class);

        ProductDaoException exception = assertThrows(ProductDaoException.class, () -> {
            productDao.getProducts();
        });

        assertEquals("Failed to get products due to unexpected error", exception.getMessage());
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockSession).close();
    }

    @Test
    public void getByIdTest() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(product);
        doNothing().when(mockSession).close();

        Product actualRes = productDao.getById(2);

        assertEquals(product, actualRes);
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).setParameter(anyString(), any());
        verify(mockQuery).uniqueResult();
        verify(mockSession).close();
    }


    @Test
    public void getByIdTest_getProductDaoException_throwProductDaoException() {
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
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        assertTrue(productDao.save(product));

        verify(mockSession).save(product);
        verify(mockTransaction).commit();
        verify(mockSession).close();
    }


    @Test
    public void saveProduct_failTest() {
        doThrow(new HibernateException("Failed to save product")).when(mockSession).save(product);

        assertThrows(ProductDaoException.class, () -> {
            productDao.save(product);
        });

        verify(mockSessionFactory).openSession();
        verify(mockSession).beginTransaction();
        verify(mockSession).save(product);
        verify(mockTransaction, never()).commit();
        verify(mockTransaction).rollback();
        verify(mockSession).close();
    }

    @Test
    public void updateNameTest() {
        long id = 2;
        String newName = "New name";

        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        productDao.updateName(id, newName);

        verify(mockSessionFactory).openSession();
        verify(mockSession).beginTransaction();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery, times(2)).setParameter(anyString(), any());
        verify(mockSession).close();
        verify(mockTransaction).commit();
        verify(mockQuery).executeUpdate();
    }

    @Test
    public void updateDescriptionTest() {
        long id = 2;
        String newDescription = "New description";
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
    public void searchByDescriptionTest() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(res);
        doNothing().when(mockSession).close();

        List<Product> actualRes = productDao.searchByDescription("Test");

        assertEquals(res, actualRes);
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).setParameter(anyString(), any());
        verify(mockSession).close();
    }

    @Test
    public void getPictureUrlTest() {
        when(mockSession.get(eq(Product.class), anyLong())).thenReturn(product);
        doNothing().when(mockSession).close();

        String actualRes = productDao.getPictureUrl(product.getId());
        assertEquals(product.getPictureUrl(), actualRes);

    }

    @Test
    public void savePictureUrlTest() {
        when(mockSession.get(eq(Product.class), anyLong())).thenReturn(product);
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
