package org.antontech.repository;

import org.antontech.model.Product;
import org.antontech.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectJDBCDao implements IProjectDao {
    private static final String DB_URL = "jdbc:postgresql://localhost:5430/antontech_db";
    private static final String USER = "admin";
    private static final String PASS = "ziwei123!";
    private static final Logger log = LoggerFactory.getLogger(ProjectJDBCDao.class);

    @Override
    public List<Project> getProjects() {
        log.info("Start to getProject from postgres via JDBC");

        List<Project> projects = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        log.debug("setup required attribute");

        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT * FROM projects";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            log.info("Connects to DB and execute the query");

            while (rs.next()) {
                int project_id = rs.getInt("project_id");
                int oem = rs.getInt("oem");
                int supplier = rs.getInt("supplier");
                Date start_date = rs.getDate("start_date");
                String description = rs.getString("description");
                String manager = rs.getString("manager");
                log.info("Get all attributes and translate to java" + project_id);

                Project project = new Project();
                project.setProject_id(project_id);
                project.setOem(oem);
                project.setSupplier(supplier);
                project.setStart_date(start_date);
                project.setDescription(description);
                project.setManager(manager);
                projects.add(project);
            }
        } catch (SQLException e){
            log.error("Unable to connect to db or execute query", e);
        } finally {
            try {
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
                if(rs != null) rs.close();
                if(con != null) con.close();
            } catch (SQLException e) {

            }
        }
        return projects;
    }

    @Override
    public boolean save(Project project) {
        return false;
    }

    @Override
    public Project getById(long id) {
        return null;
    }

    @Override
    public void updateDescription(long id, String description) {

    }

    @Override
    public void updateManager(long id, String manager) {

    }

    @Override
    public void delete(long id) {

    }
}
