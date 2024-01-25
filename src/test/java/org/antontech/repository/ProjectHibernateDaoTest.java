package org.antontech.repository;

import org.antontech.model.Project;
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

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProjectHibernateDaoTest {
    @Mock
    private SessionFactory mockSessionFactory;
    @Mock
    private Session mockSession;
    @Mock
    private Query mockQuery;
    @Mock
    private Transaction mockTransaction;

    @Test
    public void getProjectsTest() {
        IProjectDao projectDao = new ProjectHibernateDao();
        Project project = new Project(1, new Date(System.currentTimeMillis()), "Test Description", "TestManager" );
        List<Project> res = List.of(project);
        try(MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.list()).thenReturn(res);
            doNothing().when(mockSession).close();
            List<Project> actualRes = projectDao.getProjects();
            assertEquals(res, actualRes);
        }
    }

    @Test
    public void getProjectsTest_getHibernateException_throwHibernateException() {
        IProjectDao projectDao = new ProjectHibernateDao();
        Project project = new Project(1, new Date(System.currentTimeMillis()), "Test Description", "TestManager" );
        List<Project> res = List.of(project);
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.list()).thenReturn(res);
            doThrow(HibernateException.class).when(mockSession).close();
            assertThrows(HibernateException.class, () -> projectDao.getProjects());
        }
    }

    @Test
    public void save_happyPass() {
        IProjectDao projectDao = new ProjectHibernateDao();
        Project project = new Project(1, new Date(System.currentTimeMillis()), "Test Description", "TestManager" );

        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            doNothing().when(mockTransaction).commit();
            doNothing().when(mockSession).close();
            boolean res = projectDao.save(project);
            assertTrue(res);
        }
    }

    @Test
    public void getByIdTest() {
        IProjectDao projectDao = new ProjectHibernateDao();
        Project project = new Project(1, new Date(System.currentTimeMillis()), "Test Description", "TestManager" );
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
            when(mockQuery.uniqueResult()).thenReturn(project);
            doNothing().when(mockSession).close();
            Project actualRes = projectDao.getById(1);
            assertEquals(project, actualRes);
        }
    }

    @Test
    public void updateDescriptionTest() {
        IProjectDao projectDao = new ProjectHibernateDao();
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
            projectDao.updateDescription(id, newDescription);
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
    public void updateManagerTest() {
        IProjectDao projectDao = new ProjectHibernateDao();
        long id = 1;
        String newManager = "New manager";
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("id"), anyLong())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("manager"), anyString())).thenReturn(mockQuery);
            when(mockQuery.executeUpdate()).thenReturn(1);
            doNothing().when(mockTransaction).commit();
            doNothing().when(mockSession).close();
            projectDao.updateManager(id, newManager);
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
        IProjectDao projectDao = new ProjectHibernateDao();
        long id = 1;
        try (MockedStatic mockedStatic = mockStatic(HibernateUtil.class)) {
            mockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(mockSessionFactory);
            when(mockSessionFactory.openSession()).thenReturn(mockSession);
            when(mockSession.beginTransaction()).thenReturn(mockTransaction);
            when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
            when(mockQuery.setParameter(eq("id"), anyLong())).thenReturn(mockQuery);
            when(mockQuery.executeUpdate()).thenReturn(1);
            doNothing().when(mockTransaction).commit();
            doNothing().when(mockSession).close();
            projectDao.delete(id);
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
