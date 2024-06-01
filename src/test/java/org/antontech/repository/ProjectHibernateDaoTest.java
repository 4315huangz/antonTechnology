package org.antontech.repository;

import org.antontech.ApplicationBootstrap;
import org.antontech.model.Product;
import org.antontech.model.Project;
import org.antontech.repository.Exception.ProjectDaoException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class ProjectHibernateDaoTest {
    @MockBean
    private SessionFactory mockSessionFactory;
    @Mock
    private Session mockSession;
    @Mock
    private Query mockQuery;
    @Mock
    private Transaction mockTransaction;
    @Autowired
    private ProjectHibernateDao projectDao;

    private Project project = new Project(1, new Date(System.currentTimeMillis()), "Test Description", "TestManager");
    private List<Project> res = List.of(project);

    @Test
    public void getProjectsTest() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(res);
        doNothing().when(mockSession).close();

        List<Project> actualRes = projectDao.getProjects();

        assertEquals(res, actualRes);
        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockQuery, times(1)).list();
        verify(mockSession, times(1)).close();
    }

    @Test
    public void getProjectsTest_getProjectDaoExceptionException_throwProjectDaoExceptionException() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenThrow(HibernateException.class);

        ProjectDaoException exception = assertThrows(ProjectDaoException.class, () ->
                projectDao.getProjects());

        assertEquals("Failed to get projects due to unexpected error", exception.getMessage());
        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockSession, times(1)).close();
    }

    @Test
    public void save_happyPass() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        doNothing().when(mockSession).saveOrUpdate(project);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        boolean res = projectDao.save(project);

        assertTrue(res);
        verify(mockSession).saveOrUpdate(project);
        verify(mockTransaction).commit();
        verify(mockSession).close();
    }

    @Test
    public void save_failTest() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        doThrow(new HibernateException("Mock Exception")).when(mockSession).saveOrUpdate(project);

        HibernateException exception = assertThrows(HibernateException.class, () ->
                projectDao.getProjects());

        assertEquals("Mock exception", exception.getMessage());
        verify(mockTransaction, never()).commit();
        verify(mockTransaction).rollback();
        verify(mockSession, never()).close();
    }

    @Test
    public void getByIdTest() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(project);
        doNothing().when(mockSession).close();
        assertEquals(project, projectDao.getById(1));

    }

    @Test
    public void updateDescriptionTest() {
        long id = 2;
        String newDescription = "New description";
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

    @Test
    public void updateManagerTest() {
        long id = 1;
        String newManager = "New manager";
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

    @Test
    public void deleteTest() {
        long id = 1;
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
