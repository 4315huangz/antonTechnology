package org.antontech.repository;
import org.antontech.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

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
            log.info("Connects to DB and execute the select projects query");

            while (rs.next()) {
                int project_id = rs.getInt("project_id");
                Date start_date = rs.getDate("start_date");
                String description = rs.getString("description");
                String manager = rs.getString("manager");
                log.info("Get all attributes and translate to java object " + project_id);

                Project project = new Project();
                project.setProjectId(project_id);
                project.setStartDate(start_date);
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
                log.error("Unable to close the JDBC Connection",e);
            }
        }
        return projects;
    }


    @Override
    public boolean save(Project project) {
        log.info("Start to create Project in postgres via JDBC");
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        log.debug("Setup required attributes");

        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO projects (start_date, description, manager) VALUES (?,?,?)";
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            log.info("Connects to DB and execute the insert query");
            java.util.Date utilDate = project.getStartDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            stmt.setDate(1, sqlDate );
            stmt.setString(2, project.getDescription());
            stmt.setString(3, project.getManager());
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                long generatedId = rs.getLong(1);
                project.setProjectId(generatedId);
                return true;
            }
        } catch (SQLException e){
            log.error("Unable to connect to db or execute update", e);
        } finally {
            try {
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
                if(con != null) con.close();
            } catch (SQLException e) {
                log.error("Unable to close the JDBC Connection",e);
            }
        }
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
        log.info("Start to delete Project in postgres via JDBC");
        Connection con = null;
        PreparedStatement ps = null;
        int rowDeleted;
        log.debug("Setup required attributes");

        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "DELETE FROM projects WHERE project_id = ?";
            ps = con.prepareStatement(sql);
            log.info("Connects to DB and execute the delete query");
            ps.setLong(1, id);
            rowDeleted = ps.executeUpdate();
            if(rowDeleted > 0)
                log.info("Project {} deleted successfully.", id);
            else
                log.info("Failed to delete project {}", id);
        } catch (SQLException e){
            log.error("Unable to connect to db or execute delete", e);
        } finally {
            try {
                if(ps != null) ps.close();
                if(con != null) con.close();
            } catch (SQLException e) {
                log.error("Unable to close the JDBC Connection",e);
            }
        }
    }
}
