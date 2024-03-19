package org.antontech.service;

import org.antontech.model.Role;
import org.antontech.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ResourceLoadServiceTest {
    @Mock
    RoleService mockRoleService;
    @Mock
    UserService mockUserService;
    @InjectMocks
    ResourceLoadService resourceLoadService;

    @Test
    public void loadAllowedResourcesTest() {
        User user = new User();
        user.setUserId(1);
        user.setUserName("test user");
        List<User> users = new ArrayList<>();
        users.add(user);
        Role role = new Role();
        role.setId(1);
        role.setName("Admin");
        role.setAllowedRead(true);
        role.setAllowedCreate(true);
        role.setAllowedUpdate(true);
        role.setAllowedDelete(true);
        role.setUsers(users);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        when(mockUserService.getUserSecurityById(anyLong())).thenReturn(user);
        when(mockRoleService.getAllowedReadResources(role)).thenReturn("test string");
        when(mockRoleService.getAllowedCreateResources(role)).thenReturn("test string");
        when(mockRoleService.getAllowedUpdateResources(role)).thenReturn("test string");
        when(mockRoleService.getAllowedDeleteResources(role)).thenReturn("test string");

        resourceLoadService.loadAllowedResources(1L);

        verify(mockUserService, times(1)).getUserSecurityById(anyLong());
        verify(mockRoleService, times(2)).getAllowedReadResources(role);
        verify(mockRoleService, times(2)).getAllowedCreateResources(role);
        verify(mockRoleService, times(2)).getAllowedUpdateResources(role);
        verify(mockRoleService, times(2)).getAllowedDeleteResources(role);

    }
}
