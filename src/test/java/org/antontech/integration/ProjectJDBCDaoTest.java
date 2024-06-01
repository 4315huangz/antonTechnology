package org.antontech.integration;
import junit.framework.Assert;
import org.antontech.model.Project;
import org.antontech.repository.ProjectJDBCDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProjectJDBCDaoTest {
    ProjectJDBCDao projectJDBCDao;
    Project project;

    @Before
    public void setup(){
        projectJDBCDao = new ProjectJDBCDao();
        project = new Project();
        project.setStartDate(new Date());
        project.setDescription("Test description");
        project.setManager("Test Manager");
        projectJDBCDao.save(project);
    }

    @After
    public void teardown(){
        projectJDBCDao.delete(project.getProjectId());
    }

    @Test
    public void getProjects(){
        List<Project> projectList = projectJDBCDao.getProjects();
        assertEquals(4, projectList.size());
    }

    @Test
    public void getByIdTest() {
        long newId = project.getProjectId();
        Project p = projectJDBCDao.getById(newId);
        assertEquals(newId, (long)p.getProjectId());
    }

    @Test
    public void updateDescriptionTest() {
        String newDes = "New description";
        long projectIdToBeUpdated = project.getProjectId();
        projectJDBCDao.updateDescription(projectIdToBeUpdated, newDes);
        String actual = projectJDBCDao.getById(projectIdToBeUpdated).getDescription();
        Assert.assertEquals(newDes, actual);
    }

    @Test
    public void updateManagerTest() {
        String newManager = "John Jake";
        long projectIdToBeUpdated = project.getProjectId();
        projectJDBCDao.updateManager(projectIdToBeUpdated, newManager);
        String actual = projectJDBCDao.getById(projectIdToBeUpdated).getManager();
        Assert.assertEquals(newManager, actual);
    }
}
