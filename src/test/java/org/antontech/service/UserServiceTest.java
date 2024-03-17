package org.antontech.service;

import org.antontech.dto.UserDTO;
import org.antontech.dto.UserDTOMapper;
import org.antontech.model.User;
import org.antontech.repository.UserHibernateDao;
import org.antontech.service.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserDTOMapper mockUserDTOMapper;
    @Mock
    private UserHibernateDao mockUserDao;
    @InjectMocks
    private UserService userService;

    private User user1;
    private UserDTO userDTO;

    @Before
    public void setup() {
        user1 = new User();
        user1.setUserId(1L);
        user1.setUserName("miche");
        user1.setPassword("12345678");
        user1.setFirstName("Michelle");
        user1.setLastName("Williams");
        user1.setEmail("mwilliams@gmail.com");
        user1.setCompanyName("Ford");
        user1.setAddress("US");
        user1.setIndustry("Auto");
        user1.setTitle("Manager");
        user1.setPhone("999-999-99999");
        user1.setCompanyType("OEM");
        user1.setProjects(new ArrayList<>());

        userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setName("Michelle Williams");
        userDTO.setEmail("mwilliams@gmail.com");
        userDTO.setCompany("Ford");
        userDTO.setIndustry("Auto");
        userDTO.setTitle("Associate");
        userDTO.setType("OEM");
    }

    @Test
    public void getUsersTest() {
        List<User> users = new ArrayList<>();
        users.add(user1);
        when(mockUserDao.getUsers()).thenReturn(users);
        when(mockUserDTOMapper.apply(user1)).thenReturn(userDTO);

        List<UserDTO> actual = userService.getUsers();

        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    public void saveTest() {
        when(mockUserDao.save(user1)).thenReturn(true);

        assertTrue(userService.save(user1));
    }

    @Test
    public void getUserDTOByIdTest() {
        when(mockUserDao.getById(anyLong())).thenReturn(user1);
        when(mockUserDTOMapper.apply(user1)).thenReturn(userDTO);

        UserDTO actual = userService.getUserDTOById(anyLong());

        assertEquals(userDTO, actual);
    }

    @Test
    public void getUserSecurityByIdTest() {
        when(mockUserDao.getById(anyLong())).thenReturn(user1);

        User actual = userService.getUserSecurityById(anyLong());

        assertEquals(user1, actual);
    }

    @Test
    public void getByIndustryTest() {
        List<User> users = new ArrayList<>();
        users.add(user1);
        when(mockUserDao.getByIndustry(anyString())).thenReturn(users);
        when(mockUserDTOMapper.apply(user1)).thenReturn(userDTO);

        List<UserDTO> expected = new ArrayList<>();
        expected.add(userDTO);
        List<UserDTO> actual = userService.getByIndustry("Auto");
        assertEquals(expected, actual);
    }

    @Test
    public void updateEmailTest() {
        userService.updateEmail(anyLong(), anyString());

        verify(mockUserDao,times(1)).updateEmail(anyLong(), anyString());
    }

    @Test
    public void updatePassword() {
        userService.updatePassword(anyLong(), anyString());

        verify(mockUserDao, times(1)).updatePassword(anyLong(), anyString());
    }

    @Test
    public void updateCompanyTest() {
        userService.updateCompany(anyLong(), anyString(), anyString(), anyString());

        verify(mockUserDao, times(1)).updateCompany(anyLong(), anyString(), anyString(), anyString());
    }

    @Test
    public void updateManagerTest() {
        userService.updateManager(anyLong(), anyString(), anyString(), anyString(), anyString());

        verify(mockUserDao, times(1)).updateManager(anyLong(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void deleteTest() {
        userService.delete(anyLong());

        verify(mockUserDao, times(1)).delete(anyLong());
    }

    @Test
    public void getUserByCredentialsTest() {
            when(mockUserDao.getUserByCredentials(anyString(), anyString())).thenReturn(user1);
            User actual = userService.getUserByCredentials(anyString(), anyString());

            assertEquals(user1, actual);
    }

    @Test
    public void getUserByCredentialsTest_throwResourceNotFoundException() {
        when(mockUserDao.getUserByCredentials(anyString(), anyString()))
                .thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByCredentials("email", "password"));
    }
}
