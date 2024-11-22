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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
        project.getUsers().add(user1);
        user1.getProjects().add(project);


        projectDTO = new ProjectDTO();
        projectDTO.setProjectId(1L);
        projectDTO.setStartDate(new Date());
        projectDTO.setDescription("Test project");
        projectDTO.setManager("Huang");
        projectDTO.setParticipateId(Collections.singletonList(1L));
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
        String subject = "Project Created Notification";
        String body = "Dear Michelle Williams,\n\nThe project with ID 0 has been successfully created.";

        when(mockProjectDao.save(any(Project.class))).thenReturn(true);
        when(mockUserDao.getById(anyLong())).thenReturn(user1);
        when(mockUserDao.save(any(User.class))).thenReturn(true);
        doNothing().when(mockEmailService).sendEmail(anyString(), anyString(), anyString());

        assertTrue(projectService.save(projectDTO));
        verify(mockProjectDao, times(2)).save(any(Project.class));
        verify(mockUserDao, times(1)).getById(project.getProjectId());
        verify(mockUserDao, times(1)).save(user1);
        verify(mockEmailService, times(1)).sendEmail(user1.getEmail(), subject, body);
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
    public void addProjectParticipatesTest_existingUser() {
        long projectId = 1L;
        long userId = 1L;
        User user2 = new User();
        user2.setUserId(userId);
        user2.setFirstName("test");
        user2.setLastName("test");
        user2.setEmail("test@gmail.com");
        user2.setProjects(new ArrayList<>());

        Project project2 = new Project();
        project2.setProjectId(projectId);
        project2.setUsers(new ArrayList<>());
        project2.getUsers().add(user2);
        user2.getProjects().add(project2);

        when(mockUserDao.getById(userId)).thenReturn(user2);
        when(mockProjectDao.getById(projectId)).thenReturn(project2);
        when(mockProjectDao.save(any(Project.class))).thenReturn(false);
        when(mockUserDao.save(any(User.class))).thenReturn(false);

        projectService.addProjectParticipates(projectId, userId);

        verify(mockUserDao, times(2)).getById(anyLong());
        verify(mockProjectDao, times(2)).getById(anyLong());
        verify(mockProjectDao, never()).save(project2);
        verify(mockUserDao, never()).save(user2);
        verify(mockEmailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void addProjectParticipatesTest_successful() {
        long projectId = 1L;
        long userId = 1L;
        User user2 = new User();
        user2.setUserId(userId);
        user2.setFirstName("test");
        user2.setLastName("test");
        user2.setEmail("test@gmail.com");
        user2.setProjects(new ArrayList<>());

        Project project2 = new Project();
        project2.setProjectId(projectId);
        project2.setUsers(new ArrayList<>());

        when(mockUserDao.getById(userId)).thenReturn(user2);
        when(mockProjectDao.getById(projectId)).thenReturn(project2);
        when(mockProjectDao.save(any(Project.class))).thenReturn(true);
        when(mockUserDao.save(any(User.class))).thenReturn(true);

        projectService.addProjectParticipates(projectId, userId);

        verify(mockUserDao, times(2)).getById(anyLong());
        verify(mockProjectDao, times(2)).getById(anyLong());
        verify(mockProjectDao, times(1)).save(project2);
        verify(mockUserDao, times(1)).save(user2);
        verify(mockEmailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void deleteProjectParticipatesTest_userInProject() {
        long projectId = 1L;
        long userId = 1L;
        User user2 = new User();
        user2.setUserId(userId);
        user2.setFirstName("test");
        user2.setLastName("test");
        user2.setEmail("test@gmail.com");
        user2.setProjects(new ArrayList<>());

        Project project2 = new Project();
        project2.setProjectId(projectId);
        project2.setUsers(new ArrayList<>());
        project2.getUsers().add(user2);
        user2.getProjects().add(project2);

        when(mockUserDao.getById(anyLong())).thenReturn(user2);
        when(mockProjectDao.getById(anyLong())).thenReturn(project2);
        when(mockProjectDao.save(any(Project.class))).thenReturn(true);
        when(mockUserDao.save(any(User.class))).thenReturn(true);

        projectService.deleteProjectParticipates(projectId,userId);

        verify(mockUserDao, times(2)).getById(anyLong());
        verify(mockUserDao, times(1)).save(user2);
        verify(mockProjectDao, times(2)).getById(anyLong());
        verify(mockProjectDao, times(1)).save(project2);
        verify(mockEmailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void deleteProjectParticipatesTest_userNotInProject() {
        long projectId = 1L;
        long userId = 1L;
        User user2 = new User();
        user2.setUserId(userId);
        user2.setFirstName("test");
        user2.setLastName("test");
        user2.setEmail("test@gmail.com");
        user2.setProjects(new ArrayList<>());

        Project project2 = new Project();
        project2.setProjectId(projectId);
        project2.setUsers(new ArrayList<>());

        when(mockUserDao.getById(projectId)).thenReturn(user2);
        when(mockProjectDao.getById(userId)).thenReturn(project2);

        projectService.deleteProjectParticipates(projectId,userId);

        verify(mockUserDao, times(2)).getById(anyLong());
        verify(mockUserDao, never()).save(user1);
        verify(mockProjectDao, times(2)).getById(anyLong());
        verify(mockProjectDao, never()).save(project);
        verify(mockEmailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void deleteTest() {
        projectService.delete(anyLong());

        verify(mockProjectDao, times(1)).delete(anyLong());
    }
}
