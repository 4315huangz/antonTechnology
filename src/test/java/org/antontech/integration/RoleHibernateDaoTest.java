package org.antontech.integration;

import org.antontech.ApplicationBootstrap;
import org.antontech.model.Role;
import org.antontech.repository.IRoleDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class RoleHibernateDaoTest {
    @Autowired
    IRoleDao roleDao;
    Role role;

    @Before
    public void setUp() {
        role = new Role();
        role.setName("Test Role");
        role.setAllowedResource("/testRoleCreation");
        role.setAllowedRead(false);
        role.setAllowedCreate(true);
        role.setAllowedUpdate(false);
        role.setAllowedDelete(true);
        roleDao.save(role);
    }

    @After
    public void tearDown(){
        roleDao.delete(role);
    }

    @Test
    public void getRolesTest() {
        List<Role> roles = roleDao.getRoles();
        assertEquals(8,roles.size());
    }

    @Test
    public void getAllowedResourcesTest() {
        String testRes = roleDao.getAllowedResources(role);
        assertEquals(role.getAllowedResource(), testRes);
    }

    @Test
    public void getAllowedReadResources() {
        assertEquals("No allowed read resources found", roleDao.getAllowedReadResources(role));
    }

    @Test
    public void getAllowedCreateResources() {
        assertEquals(role.getAllowedResource(), roleDao.getAllowedCreateResources(role));
    }

    @Test
    public void getAllowedUpdateResources() {
        assertEquals("No allowed update resources found", roleDao.getAllowedUpdateResources(role));
    }

    @Test
    public void getAllowedDeleteResources() {
        assertEquals(role.getAllowedResource(), roleDao.getAllowedDeleteResources(role));
    }
}
