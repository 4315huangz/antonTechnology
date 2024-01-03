package org.antontech.repository;

import org.antontech.model.Product;
import org.antontech.model.Project;

import java.sql.SQLException;
import java.util.List;

public interface IProjectDao {
    List<Project> getProjects() throws SQLException;

    boolean save(Project project);

    Project getById(long id);

    void updateDescription(long id, String description);

    void updateManager(long id, String manager);

    void delete(long id);

}
