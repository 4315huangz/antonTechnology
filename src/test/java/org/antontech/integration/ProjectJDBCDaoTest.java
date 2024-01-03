package org.antontech.integration;

import org.antontech.model.Product;
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
        project.setOem(3);
        project.setSupplier(16);
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
        assertEquals(1, projectList.size());
    }

}
