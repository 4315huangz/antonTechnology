package org.antontech.repository;

import org.antontech.model.Project;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProjectsJDBCDaoTest {
    ProjectJDBCDao projectJDBCDao;

    @Before
    public void setup(){
        projectJDBCDao = new ProjectJDBCDao();
    }

    @After
    public void teardown(){

    }

    @Test
    public void getProjects(){
        List<Project> projectList = projectJDBCDao.getProjects();
        assertEquals(0, projectList.size());
    }

}
