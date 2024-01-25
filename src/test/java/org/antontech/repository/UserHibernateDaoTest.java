package org.antontech.repository;

import org.antontech.model.User;
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

    @Test
    public void getUsersTest() {
    IUserDao userDao = new UserHibernateDao();
    User user = new User(5, "testUser", "testPassword", "Amy", "Michele",
            "amymichele@gmail.com","ABC Inc","US","Consulting","Manger",
            "999-999-9999","OEM");
    List<User> res = List.of(user);
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.list()).thenReturn(res);
            doNothing().when(mockSession).close();

            List<User> actualRes = userDao.getUsers();
            assertEquals(res, actualRes);
        }
    }

    @Test
    public void getUsersTest_getHibernateException_throwHibernateException(){
        IUserDao userDao = new UserHibernateDao();
        User user = new User(5, "testUser", "testPassword", "Amy", "Michele",
                "amymichele@gmail.com","ABC Inc","US","Consulting","Manger",
                "999-999-9999","OEM");
        List<User> res = List.of(user);
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.list()).thenReturn(res);
            doThrow(HibernateException.class).when(mockSession).close();
            assertThrows(HibernateException.class, () -> userDao.getUsers());
        }
    }

    @Test
    public void save_happyPass() {
        IUserDao userDao = new UserHibernateDao();
        User user = new User(5, "testUser", "testPassword", "Amy", "Michele",
                "amymichele@gmail.com","ABC Inc","US","Consulting","Manger",
                "999-999-9999","OEM");
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            doNothing().when(mockTransaction).commit();
            doNothing().when(mockSession).close();
            boolean res = userDao.save(user);
            assertTrue(res);
        }
    }

    @Test
    public void getByIdTest() {
        IUserDao userDao = new UserHibernateDao();
        User user = new User(5, "testUser", "testPassword", "Amy", "Michele",
                "amymichele@gmail.com", "ABC Inc", "US", "Consulting", "Manger",
                "999-999-9999", "OEM");
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
            when(mockQuery.uniqueResult()).thenReturn(user);
            doNothing().when(mockSession).close();
            User actualRes = userDao.getById(5);
            assertEquals(user, actualRes);
        }
    }

    @Test
    public void getByIndustryTest() {
        IUserDao userDao = new UserHibernateDao();
        User user = new User(5, "testUser", "testPassword", "Amy", "Michele",
                "amymichele@gmail.com","ABC Inc","US","Consulting","Manger",
                "999-999-9999","OEM");
        List<User> res = List.of(user);
        String industry = "Consulting";
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq(industry), anyString())).thenReturn(mockQuery);
            when(mockQuery.list()).thenReturn(res);
            doNothing().when(mockSession).close();
            List<User> actualRes = userDao.getByIndustry(industry);
            assertEquals(res, actualRes);
        }
    }

    @Test
    public void updateEmailTest(){
        IUserDao userDao = new UserHibernateDao();
        long id=5;
        String newEmail = "amymichele@update.com";
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("id"), anyLong())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("email"), anyString())).thenReturn(mockQuery);
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
    }

    @Test
    public void updatePasswordTest(){
        IUserDao userDao = new UserHibernateDao();
        long id=5;
        String newPW = "updatedPassword";
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("id"), anyLong())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("password"), anyString())).thenReturn(mockQuery);
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
    }

    @Test
    public void updateCompanyTest(){
        IUserDao userDao = new UserHibernateDao();
        long id=5;
        String newCompany = "Company LLC";
        String newAddress = "CN";
        String newIndustry = "updated industry";
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("id"), anyLong())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("companyName"), anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("Address"), anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("industry"),anyString())).thenReturn(mockQuery);
            when(mockQuery.executeUpdate()).thenReturn(1);
            doNothing().when(mockTransaction).commit();
            doNothing().when(mockSession).close();
            userDao.updateCompany(id, newCompany,newAddress,newIndustry);
            verify(mockSessionFactory, times(1)).openSession();
            verify(mockSession, times(1)).beginTransaction();
            verify(mockSession, times(1)).createQuery(anyString());
            verify(mockQuery, times(4)).setParameter(anyString(), any());
            verify(mockSession, times(1)).close();
            verify(mockTransaction, times(1)).commit();
            verify(mockQuery, times(1)).executeUpdate();
        }
    }

    @Test
    public void updateManagerTest(){
        IUserDao userDao = new UserHibernateDao();
        long id=5;
        String newFirstName = "Jack";
        String newLastName = "John";
        String newTitle = "Senior";
        String newPhone = "888-888-8888";
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("id"), anyLong())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("firstName"), anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("lastName"), anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("title"),anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("phone"),anyString())).thenReturn(mockQuery);
            when(mockQuery.executeUpdate()).thenReturn(1);
            doNothing().when(mockTransaction).commit();
            doNothing().when(mockSession).close();
            userDao.updateManager(id, newFirstName,newLastName,newTitle,newPhone);
            verify(mockSessionFactory, times(1)).openSession();
            verify(mockSession, times(1)).beginTransaction();
            verify(mockSession, times(1)).createQuery(anyString());
            verify(mockQuery, times(5)).setParameter(anyString(), any());
            verify(mockSession, times(1)).close();
            verify(mockTransaction, times(1)).commit();
            verify(mockQuery, times(1)).executeUpdate();
        }
    }

    @Test
    public void deleteTest(){
        IUserDao userDao = new UserHibernateDao();
        long id=5;
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("id"), anyLong())).thenReturn(mockQuery);
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
}
