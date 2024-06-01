package org.antontech.repository;

import org.antontech.ApplicationBootstrap;
import org.antontech.model.Project;
import org.antontech.repository.Exception.ProjectDaoException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

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

    @Before
    public void setup() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
    }

    @Test
    public void getProjectsTest() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(res);
        doNothing().when(mockSession).close();

        List<Project> actualRes = projectDao.getProjects();

        assertEquals(res, actualRes);
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).list();
        verify(mockSession).close();
    }

    @Test
    public void getProjectsTest_getProjectDaoExceptionException_throwProjectDaoExceptionException() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenThrow(HibernateException.class);

        ProjectDaoException exception = assertThrows(ProjectDaoException.class, () ->
                projectDao.getProjects());

        assertEquals("Failed to get projects due to unexpected error", exception.getMessage());
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockSession).close();
    }

    @Test
    public void save_happyPass() {
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
        doThrow(new HibernateException("Mock Exception")).when(mockSession).saveOrUpdate(project);

        assertThrows(ProjectDaoException.class, () -> {
                projectDao.save(project);
        });

        verify(mockSessionFactory).openSession();
        verify(mockSession).beginTransaction();
        verify(mockSession).saveOrUpdate(project);
        verify(mockTransaction, never()).commit();
        verify(mockTransaction).rollback();
        verify(mockSession).close();
    }

    @Test
    public void getByIdTest() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(project);
        doNothing().when(mockSession).close();

        assertEquals(project, projectDao.getById(1));
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).uniqueResult();
        verify(mockSession).close();

    }

    @Test
    public void updateDescriptionTest() {
        long id = 2;
        String newDescription = "New description";
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(eq("id"), anyLong())).thenReturn(mockQuery);
        when(mockQuery.setParameter(eq("description"), anyString())).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        projectDao.updateDescription(id, newDescription);

        verify(mockSessionFactory).openSession();
        verify(mockSession).beginTransaction();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery, times(2)).setParameter(anyString(), any());
        verify(mockSession).close();
        verify(mockTransaction).commit();
        verify(mockQuery).executeUpdate();
    }

    @Test
    public void updateManagerTest() {
        long id = 1;
        String newManager = "New manager";
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(eq("id"), anyLong())).thenReturn(mockQuery);
        when(mockQuery.setParameter(eq("manager"), anyString())).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();
        projectDao.updateManager(id, newManager);

        verify(mockSessionFactory).openSession();
        verify(mockSession).beginTransaction();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery, times(2)).setParameter(anyString(), any());
        verify(mockSession).close();
        verify(mockTransaction).commit();
        verify(mockQuery).executeUpdate();
    }

    @Test
    public void deleteTest() {
        long id = 1;
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
