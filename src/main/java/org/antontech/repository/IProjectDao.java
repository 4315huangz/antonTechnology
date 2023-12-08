package org.antontech.repository;

import org.antontech.model.Product;
import org.antontech.model.Project;

import java.sql.SQLException;
import java.util.List;

public interface IProjectDao {
    List<Project> getProjects() throws SQLException;

    //Create project
    boolean save(Project project);

    //Search project by id
    Project getById(int id);

    //Update project description
    void updateDescription(int id, String description);

    //Update project manager
    void updateManager(int id, String manager);

    //Delete project
    void delete(int id);

}
