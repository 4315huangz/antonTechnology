package org.antontech.repository;

import org.antontech.model.Project;

import java.sql.SQLException;
import java.util.List;

public interface IProjectDao {
    List<Project> getProjects() throws SQLException;

}
