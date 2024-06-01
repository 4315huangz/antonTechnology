package org.antontech.integration;

import junit.framework.Assert;
import org.antontech.ApplicationBootstrap;
import org.antontech.model.Project;
import org.antontech.model.User;
import org.antontech.repository.IUserDao;
import org.antontech.repository.ProjectHibernateDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class ProjectHibernateDaoTest {
    @Autowired
    private ProjectHibernateDao projectHibernateDao;
    @Autowired
    private IUserDao userHibernateDao;
    private Project project;
    private User user;

    @Before
    public void setup(){
        user = new User();
        user.setUserName("Michelle2024");
        user.setPassword("12345678");
        user.setFirstName("Michelle");
        user.setLastName("White");
        user.setEmail("michelle@emai.com");
        user.setCompanyName("Google INC");
        user.setAddress("Texas");
        user.setIndustry("Bank");
        user.setTitle("Teller");
        user.setPhone("000-000-0000");
        user.setCompanyType("OEM");
        userHibernateDao.save(user);

        project = new Project();
        project.setStartDate(new Date());
        project.setDescription("Test the project description");
        project.setManager("Anton Technology LLC");
        projectHibernateDao.save(project);
    }

    @After
    public  void teardown(){
        projectHibernateDao.delete(project.getProjectId());
        userHibernateDao.delete(user.getUserId());
    }

    @Test
    public void getProjectsTest() throws SQLException {
        List<Project> projectList = projectHibernateDao.getProjects();
        assertEquals(8, projectList.size());
    }

    @Test
    public void updateDescriptionTest() {
        long projectId = project.getProjectId();
        String newDesc = "Updated project description";
        projectHibernateDao.updateDescription(projectId, newDesc);
        String updatedDesc = projectHibernateDao.getById(projectId).getDescription();
        Assert.assertEquals(newDesc, updatedDesc);
    }

    @Test
    public  void updateManagerTest(){
        long projectId = project.getProjectId();
        String newManager = "Updated Manager";
       projectHibernateDao.updateManager(projectId, newManager);
       String updatedManager = projectHibernateDao.getById(projectId).getManager();
       Assert.assertEquals(newManager, updatedManager);
    }
}
