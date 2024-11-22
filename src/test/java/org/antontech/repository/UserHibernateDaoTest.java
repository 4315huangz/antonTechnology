package org.antontech.repository;

import org.antontech.model.User;
import org.antontech.repository.Exception.UserDaoException;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserHibernateDaoTest {
    @Mock
    SessionFactory mockSessionFactory;
    @Mock
    Session mockSession;
    @Mock
    Transaction mockTransaction;
    @Mock
    Query mockQuery;
    @InjectMocks
    UserHibernateDao userDao;

    User user = new User(5, "testUser", "testPassword", "Amy", "Michele",
            "amymichele@gmail.com", "ABC Inc", "US", "Consulting", "Manger",
            "999-999-9999", "OEM");
    List<User> res = List.of(user);

    @Before
    public void setup() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
    }

    @Test
    public void getUsersTest() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(res);
        doNothing().when(mockSession).close();

        assertEquals(res, userDao.getUsers());
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).list();
        verify(mockSession).close();
    }

    @Test
    public void getUsersTest_getHibernateException_throwHibernateException() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenThrow(HibernateException.class);

        UserDaoException exception = assertThrows(UserDaoException.class, () -> {
            userDao.getUsers();
        });

        assertEquals("Failed to get users", exception.getMessage());
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockSession).close();
    }

    @Test
    public void save_happyPass() {
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        assertTrue(userDao.save(user));

        verify(mockSession).saveOrUpdate(user);
        verify(mockTransaction).commit();
        verify(mockSession).close();
    }

    @Test
    public void save_failTest() {
        doThrow(new HibernateException("Failed to save user")).when(mockSession).saveOrUpdate(user);

        assertThrows(UserDaoException.class, () -> {
            userDao.save(user);
        });

        verify(mockSessionFactory).openSession();
        verify(mockSession).beginTransaction();
        verify(mockSession).saveOrUpdate(user);
        verify(mockTransaction, never()).commit();
        verify(mockTransaction).rollback();
        verify(mockSession).close();
    }

    @Test
    public void getByIdTest() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(user);
        doNothing().when(mockSession).close();

        User actualRes = userDao.getById(5);

        assertEquals(user, actualRes);
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).setParameter(anyString(), any());
        verify(mockQuery).uniqueResult();
        verify(mockSession).close();
    }

    @Test
    public void getByIndustryTest() {
        String industry = "Consulting";
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(res);
        doNothing().when(mockSession).close();

        List<User> actualRes = userDao.getByIndustry(industry);

        assertEquals(res, actualRes);
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).setParameter(anyString(), any());
        verify(mockQuery).list();
        verify(mockSession).close();
    }

    @Test
    public void updateEmailTest() {
        long id = 5;
        String newEmail = "amymichele@update.com";
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        userDao.updateEmail(id, newEmail);

        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).beginTransaction();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockQuery, times(2)).setParameter(anyString(), any());
        verify(mockSession, times(1)).close();
        verify(mockTransaction, times(1)).commit();
        verify(mockQuery, times(1)).executeUpdate();

    }

    @Test
    public void updatePasswordTest() {
        long id = 5;
        String newPW = "updatedPassword";
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        userDao.updatePassword(id, newPW);

        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).beginTransaction();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockQuery, times(2)).setParameter(anyString(), any());
        verify(mockSession, times(1)).close();
        verify(mockTransaction, times(1)).commit();
        verify(mockQuery, times(1)).executeUpdate();

    }

    @Test
    public void updateCompanyTest() {
        long id = 5;
        String newCompany = "Company LLC";
        String newAddress = "CN";
        String newIndustry = "updated industry";
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        userDao.updateCompany(id, newCompany, newAddress, newIndustry);

        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).beginTransaction();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockQuery, times(4)).setParameter(anyString(), any());
        verify(mockSession, times(1)).close();
        verify(mockTransaction, times(1)).commit();
        verify(mockQuery, times(1)).executeUpdate();

    }

    @Test
    public void updateManagerTest() {
        long id = 5;
        String newFirstName = "Jack";
        String newLastName = "John";
        String newTitle = "Senior";
        String newPhone = "888-888-8888";
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        userDao.updateManager(id, newFirstName, newLastName, newTitle, newPhone);

        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).beginTransaction();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockQuery, times(5)).setParameter(anyString(), any());
        verify(mockSession, times(1)).close();
        verify(mockTransaction, times(1)).commit();
        verify(mockQuery, times(1)).executeUpdate();

    }

    @Test
    public void deleteTest() {
        long id = 5;
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        userDao.delete(id);

        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).beginTransaction();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockQuery, times(1)).setParameter(anyString(), any());
        verify(mockQuery, times(1)).executeUpdate();
        verify(mockSession, times(1)).close();
        verify(mockTransaction, times(1)).commit();

    }
}
