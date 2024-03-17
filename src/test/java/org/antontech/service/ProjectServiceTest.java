package org.antontech.service;

import org.antontech.dto.ProjectDTO;
import org.antontech.dto.ProjectDTOMapper;
import org.antontech.model.Project;
import org.antontech.model.User;
import org.antontech.repository.ProjectHibernateDao;
import org.antontech.repository.UserHibernateDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {
    @Mock
    private ProjectHibernateDao mockProjectDao;
    @Mock
    private UserHibernateDao mockUserDao;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private ProjectDTOMapper mockProjectDTOMapper;
    @InjectMocks
    private ProjectService projectService;

    ProjectDTO projectDTO;
    Project project;
    User user1;

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

        project = new Project();
        project.setProjectId(1L);
        project.setStartDate(new Date());
        project.setDescription("Test project");
        project.setManager("Huang");
        project.setUsers(new ArrayList<>());

        projectDTO = new ProjectDTO();
        projectDTO.setProjectId(1);
        projectDTO.setStartDate(new Date());
        projectDTO.setDescription("Test project");
        projectDTO.setManager("Test Manager");
        projectDTO.setParticipateId(Collections.singletonList(user1.getUserId()));
    }

    @Test
    public void getProjectsTest() {
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        when(mockProjectDao.getProjects()).thenReturn(projects);
        when(mockProjectDTOMapper.apply(project)).thenReturn(projectDTO);

        Set<ProjectDTO> actual = projectService.getProjects();

        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    public void saveTest() {
        when(mockUserDao.getById(anyLong())).thenReturn(user1);
        when(mockUserDao.save(any())).thenReturn(true);
        when(mockProjectDao.save(any())).thenReturn(true);

        boolean actual = projectService.save(projectDTO);

        assertTrue(actual);
        verify(mockUserDao, times(1)).getById(anyLong());
        verify(mockUserDao, times(1)).save(any());
        verify(mockProjectDao, times(1)).save(any());
        verify(mockEmailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void getByIdTest() {
        when(mockProjectDao.getById(anyLong())).thenReturn(project);
        when(mockProjectDTOMapper.apply(any())).thenReturn(projectDTO);

        ProjectDTO actual = projectService.getById(project.getProjectId());

        assertEquals(projectDTO, actual);
    }

    @Test
    public void updateDescriptionTest() {
        projectService.updateDescription(anyLong(), anyString());

        verify(mockProjectDao,times(1)).updateDescription(anyLong(), anyString());
    }

    @Test
    public void updateManagerTest() {
        projectService.updateManager(anyLong(), anyString());

        verify(mockProjectDao, times(1)).updateManager(anyLong(), anyString());
    }

    @Test
    public void deleteTest() {
        projectService.delete(anyLong());

        verify(mockProjectDao, times(1)).delete(anyLong());
    }

    @Test
    public void addProjectParticipatesTest() {
        when(mockUserDao.getById(anyLong())).thenReturn(user1);
        when(mockProjectDao.getById(anyLong())).thenReturn(project);

        projectService.addProjectParticipates(1L, 1L);

        verify(mockUserDao, times(2)).getById(anyLong());
        verify(mockProjectDao, times(2)).getById(anyLong());
        verify(mockProjectDao, times(1)).save(any(Project.class));
        verify(mockEmailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void deleteProjectParticipatesTest() {
        List<User> users = new ArrayList<>();
        users.add(user1);
        project.setUsers(users);
        List<Project> projects = new ArrayList<>();
        projects.add(project);
        user1.setProjects(projects);
        when(mockUserDao.getById(anyLong())).thenReturn(user1);
        when(mockProjectDao.getById(anyLong())).thenReturn(project);

        projectService.deleteProjectParticipates(1,1);

        verify(mockUserDao, times(2)).getById(anyLong());
        verify(mockUserDao, times(1)).save(any(User.class));
        verify(mockProjectDao, times(2)).getById(anyLong());
        verify(mockProjectDao, times(1)).save(any(Project.class));
        verify(mockEmailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }
}
