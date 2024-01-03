package org.antontech.integration;

import org.antontech.model.Project;
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
}
