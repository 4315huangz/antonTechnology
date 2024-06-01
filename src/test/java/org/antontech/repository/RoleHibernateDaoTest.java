package org.antontech.repository;

import org.antontech.ApplicationBootstrap;
import org.antontech.model.Role;
import org.antontech.repository.Exception.RoleDaoException;
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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class RoleHibernateDaoTest {
    @MockBean
    SessionFactory mockSessionFactory;
    @Mock
    Session mockSession;
    @Mock
    Query mockQuery;
    @Mock
    Transaction mockTransaction;
    @Autowired
    RoleHibernateDao roleDao;

    Role role = new Role((long) 123, "Supplier", "/projects,/project", true, true, true, true);
    List<Role> roles = List.of(role);

    @Before
    public void setup() {
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
    }

    @Test
    public void getRolesTest() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(roles);
        doNothing().when(mockSession).close();
        List<Role> actual = roleDao.getRoles();

        assertEquals(roles, actual);
        verify(mockSessionFactory, times(1)).openSession();
        verify(mockSession, times(1)).createQuery(anyString());
        verify(mockQuery, times(1)).list();
        verify(mockSession, times(1)).close();
    }

    @Test
    public void getRolesTest_getRoleDaoException_throwRoleDaoException() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.list()).thenThrow(HibernateException.class);

        RoleDaoException exception = assertThrows(RoleDaoException.class, () -> {
            roleDao.getRoles();
        });

        assertEquals("Failed to get roles", exception.getMessage());
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockSession).close();
    }

    @Test
    public void getByIdTest() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(role);
        doNothing().when(mockSession).close();

        assertEquals(role, roleDao.getById(123));
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).setParameter(anyString(), any());
        verify(mockQuery).uniqueResult();
        verify(mockSession).close();
    }

    @Test
    public void save_happyPass() {
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        assertTrue(roleDao.save(role));

        verify(mockSession).save(role);
        verify(mockTransaction).commit();
        verify(mockSession).close();
    }

    @Test(expected = RoleDaoException.class)
    public void save_failTest() {
        doThrow(RoleDaoException.class).when(mockSession).save(role);

        assertTrue(roleDao.save(role));
        verify(mockSessionFactory).openSession();
        verify(mockSession).beginTransaction();
        verify(mockSession).save(role);
        verify(mockTransaction, never()).commit();
        verify(mockTransaction).rollback();
        verify(mockSession).close();
    }

    @Test
    public void deleteTest() {
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(1);
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockSession).close();

        roleDao.delete(role);

        verify(mockSessionFactory).openSession();
        verify(mockSession).beginTransaction();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).setParameter(anyString(), any());
        verify(mockQuery).executeUpdate();
        verify(mockTransaction).commit();
        verify(mockSession).close();
    }

    @Test
    public void getAllowedResourcesTest() {
        String allowedResources = "/projects,/project";
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(allowedResources);
        doNothing().when(mockSession).close();

        assertEquals(allowedResources, roleDao.getAllowedResources(role));
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).setParameter(anyString(), any());
        verify(mockQuery).uniqueResult();
        verify(mockSession).close();
    }

    @Test
    public void getAllowedReadResourcesTest() {
        String allowedReadResources = "/projects,/project";
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(allowedReadResources);
        doNothing().when(mockSession).close();

        assertEquals(allowedReadResources, roleDao.getAllowedReadResources(role));
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).setParameter(anyString(), any());
        verify(mockQuery).uniqueResult();
        verify(mockSession).close();
    }

    @Test
    public void getAllowedCreateResources() {
        String allowedCreateResources = "/projects,/project";
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(allowedCreateResources);
        doNothing().when(mockSession).close();

        assertEquals(allowedCreateResources, roleDao.getAllowedCreateResources(role));
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).setParameter(anyString(), any());
        verify(mockQuery).uniqueResult();
        verify(mockSession).close();
    }

    @Test
    public void getAllowedUpdateResources() {
        String allowedUpdateResources = "/projects,/project";
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(allowedUpdateResources);
        doNothing().when(mockSession).close();

        assertEquals(allowedUpdateResources, roleDao.getAllowedUpdateResources(role));
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).setParameter(anyString(), any());
        verify(mockQuery).uniqueResult();
        verify(mockSession).close();
    }

    @Test
    public void getAllowedDeleteResources() {
        String allowedDeleteResources = "/projects,/project";
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), anyLong())).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(allowedDeleteResources);
        doNothing().when(mockSession).close();

        assertEquals(allowedDeleteResources, roleDao.getAllowedDeleteResources(role));
        verify(mockSessionFactory).openSession();
        verify(mockSession).createQuery(anyString());
        verify(mockQuery).setParameter(anyString(), any());
        verify(mockQuery).uniqueResult();
        verify(mockSession).close();
    }
}
