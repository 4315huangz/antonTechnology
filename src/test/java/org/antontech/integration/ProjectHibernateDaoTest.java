package org.antontech.integration;

import junit.framework.Assert;
import org.antontech.model.Project;
import org.antontech.model.User;
import org.antontech.repository.ProjectHibernateDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProjectHibernateDaoTest {
    ProjectHibernateDao projectHibernateDao;
    Project project;

    @Before
    public void setup(){
        projectHibernateDao = new ProjectHibernateDao();
        project = new Project();
        project.setOem(3);
        project.setSupplier(16);
        project.setStartDate(new Date());
        project.setDescription("Test the project description");
        project.setManager("Anton Technology LLC");
        projectHibernateDao.save(project);
    }

    @After
    public  void teardown(){
        projectHibernateDao.delete(project.getProjectId());
    }

    @Test
    public void getProjectsTest() throws SQLException {
        List<Project> projectList = projectHibernateDao.getProjects();
        assertEquals(1, projectList.size());
    }

    @Test
    public void updateDescriptionTest() {
        long projectId = project.getProjectId();
        String newDesc = "Updated project description";
        String originalDesc = projectHibernateDao.getById(projectId).getDescription();
        projectHibernateDao.updateDescription(projectId, newDesc);
        String updatedDesc = projectHibernateDao.getById(projectId).getDescription();
        Assert.assertEquals(newDesc, updatedDesc);

        projectHibernateDao.updateDescription(projectId, originalDesc);
    }

    @Test
    public  void updateManagerTest(){
        long projectId = project.getProjectId();
        String originalManager = project.getManager();
        String newManager = "Updated Manager";
       projectHibernateDao.updateManager(projectId, newManager);
       String updatedManager = projectHibernateDao.getById(projectId).getManager();
       Assert.assertEquals(newManager, updatedManager);

       projectHibernateDao.updateManager(projectId, originalManager);


    }
}
