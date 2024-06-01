//package org.antontech.repository;
//
//import org.antontech.model.Role;
//import org.antontech.repository.Exception.RoleDaoException;
//import org.antontech.util.HibernateUtil;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.query.Query;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class RoleHibernateDaoTest {
//    @Mock
//    SessionFactory mockSessionFactory;
//    @Mock
//    Session mockSession;
//    @Mock
//    Query mockQuery;
//    @Mock
//    Transaction mockTransaction;
//    @InjectMocks
//    RoleHibernateDao roleDao;
//
//    Role role = new Role((long) 123, "Supplier", "/projects,/project", true, true, true, true);
//    List<Role> roles = List.of(role);
//
//    @Test
//    public void getRolesTest() {
//        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
//            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
//            when(mockSessionFactory.openSession()).thenReturn(mockSession);
//            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
//            when(mockQuery.list()).thenReturn(roles);
//            doNothing().when(mockSession).close();
//            roleDao.getRoles();
//        }
//    }
//
//    @Test(expected = RoleDaoException.class)
//    public void getRolesTest_getRoleDaoException_throwRoleDaoException() {
//        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
//            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
//            when(mockSessionFactory.openSession()).thenReturn(mockSession);
//            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
//            when(mockQuery.list()).thenReturn(roles);
//            doThrow(RoleDaoException.class).when(mockSession).close();
//            roleDao.getRoles();
//        }
//    }
//
//    @Test
//    public void getByIdTest() {
//        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
//            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
//            when(mockSessionFactory.openSession()).thenReturn(mockSession);
//            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
//            when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
//            when(mockQuery.uniqueResult()).thenReturn(role);
//            doNothing().when(mockSession).close();
//            assertEquals(role, roleDao.getById(123));
//        }
//    }
//
//    @Test
//    public void save_happyPass() {
//        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
//            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
//            when(mockSessionFactory.openSession()).thenReturn(mockSession);
//            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
//            doNothing().when(mockTransaction).commit();
//            doNothing().when(mockSession).close();
//            assertTrue(roleDao.save(role));
//            verify(mockSession).save(role);
//            verify(mockTransaction).commit();
//        }
//    }
//
//    @Test(expected = RoleDaoException.class)
//    public void save_failTest() {
//        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
//            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
//            when(mockSessionFactory.openSession()).thenReturn(mockSession);
//            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
//            doThrow(RoleDaoException.class).when(mockSession).save(role);
//            assertTrue(roleDao.save(role));
//        }
//    }
//
//    @Test
//    public void deleteTest() {
//        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
//            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
//            when(mockSessionFactory.openSession()).thenReturn(mockSession);
//            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
//            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
//            when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
//            when(mockQuery.executeUpdate()).thenReturn(1);
//            doNothing().when(mockTransaction).commit();
//            doNothing().when(mockSession).close();
//            roleDao.delete(role);
//            verify(mockSessionFactory, times(1)).openSession();
//            verify(mockSession, times(1)).beginTransaction();
//            verify(mockSession, times(1)).createQuery(anyString());
//            verify(mockQuery, times(1)).setParameter(anyString(), any());
//            verify(mockQuery, times(1)).executeUpdate();
//            verify(mockTransaction, times(1)).commit();
//            verify(mockSession, times(1)).close();
//        }
//    }
//
//    @Test
//    public void getAllowedResourcesTest() {
//        String allowedResources = "/projects,/project";
//        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
//            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
//            when(mockSessionFactory.openSession()).thenReturn(mockSession);
//            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
//            when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
//            when(mockQuery.uniqueResult()).thenReturn(allowedResources);
//            doNothing().when(mockSession).close();
//            assertEquals(allowedResources, roleDao.getAllowedResources(role));
//        }
//    }
//
//    @Test
//    public void getAllowedReadResourcesTest() {
//        String allowedReadResources = "/projects,/project";
//        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
//            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
//            when(mockSessionFactory.openSession()).thenReturn(mockSession);
//            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
//            when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
//            when(mockQuery.uniqueResult()).thenReturn(allowedReadResources);
//            doNothing().when(mockSession).close();
//            assertEquals(allowedReadResources, roleDao.getAllowedReadResources(role));
//        }
//    }
//
//    @Test
//    public void getAllowedCreateResources() {
//        String allowedCreateResources = "/projects,/project";
//        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
//            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
//            when(mockSessionFactory.openSession()).thenReturn(mockSession);
//            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
//            when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
//            when(mockQuery.uniqueResult()).thenReturn(allowedCreateResources);
//            doNothing().when(mockSession).close();
//            assertEquals(allowedCreateResources, roleDao.getAllowedCreateResources(role));
//        }
//    }
//
//    @Test
//    public void getAllowedUpdateResources() {
//        String allowedUpdateResources = "/projects,/project";
//        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
//            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
//            when(mockSessionFactory.openSession()).thenReturn(mockSession);
//            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
//            when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
//            when(mockQuery.uniqueResult()).thenReturn(allowedUpdateResources);
//            doNothing().when(mockSession).close();
//            assertEquals(allowedUpdateResources, roleDao.getAllowedUpdateResources(role));
//        }
//    }
//
//    @Test
//    public void getAllowedDeleteResources() {
//        String allowedDeleteResources = "/projects,/project";
//        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
//            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
//            when(mockSessionFactory.openSession()).thenReturn(mockSession);
//            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
//            when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
//            when(mockQuery.uniqueResult()).thenReturn(allowedDeleteResources);
//            doNothing().when(mockSession).close();
//            assertEquals(allowedDeleteResources, roleDao.getAllowedDeleteResources(role));
//        }
//    }
//}
