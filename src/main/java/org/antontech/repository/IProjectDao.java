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
    Project getById(long id);

    //Update project description
    void updateDescription(long id, String description);

    //Update project manager
    void updateManager(long id, String manager);

    //Delete project
    void delete(long id);

}
