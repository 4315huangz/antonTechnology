package org.antontech.service;

import org.antontech.ApplicationBootstrap;
import org.antontech.model.Role;
import org.antontech.repository.RoleHibernateDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class RoleServiceTest {
    @Mock
    private RoleHibernateDao mockRoleDao;
    @InjectMocks
    private RoleService roleService;
    private Role role1;

    @Test
    public void getRolesTest() {
        List<Role> roles = new ArrayList<>();
        when(mockRoleDao.getRoles()).thenReturn(roles);
        roleService.getRoles();
        verify(mockRoleDao).getRoles();
    }

    @Test
    public void getByIdTest() {
        when(mockRoleDao.getById(anyLong())).thenReturn(role1);
        roleService.getById(1);
        verify(mockRoleDao).getById(1);
    }

    @Test
    public void saveTest() {
        when(mockRoleDao.save(any())).thenReturn(true);
        roleService.save(role1);
        assertTrue(mockRoleDao.save(any()));
    }

    @Test
    public void deleteTest() {
        roleService.delete(role1);
        verify(mockRoleDao).delete(any());
    }

    @Test
    public void getAllowedReadResourcesTest() {
        String allowedRead = "Test allowed read resource";
        when(mockRoleDao.getAllowedReadResources(role1)).thenReturn(allowedRead);
        String actual = roleService.getAllowedReadResources(role1);
        assertEquals(allowedRead, actual);
    }

    @Test
    public void getAllowedCreateResourcesTest() {
        String allowedCreate = "Test allowed read resource";
        when(mockRoleDao.getAllowedCreateResources(role1)).thenReturn(allowedCreate);
        String actual = roleService.getAllowedCreateResources(role1);
        assertEquals(allowedCreate, actual);
    }

    @Test
    public void getAllowedUpdateResourcesTest() {
        String allowedUpdate = "Test allowed read resource";
        when(mockRoleDao.getAllowedUpdateResources(role1)).thenReturn(allowedUpdate);
        String actual = roleService.getAllowedUpdateResources(role1);
        assertEquals(allowedUpdate, actual);
    }

    @Test
    public void getAllowedDeleteResourcesTest() {
        String allowedDelete = "Test allowed delete resource";
        when(mockRoleDao.getAllowedDeleteResources(role1)).thenReturn(allowedDelete);
        String actual = roleService.getAllowedDeleteResources(role1);
        assertEquals(allowedDelete, actual);
    }
}
