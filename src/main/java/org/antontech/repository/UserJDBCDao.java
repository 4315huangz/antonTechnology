package org.antontech.repository;

import org.antontech.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserJDBCDao implements IUserDao{
    private static final String DB_URL = "jdbc:postgresql://localhost:5430/antontech_db";
    private static final String USER = "admin";
    private static final String PASS = "ziwei123!";
    private static final Logger log = LoggerFactory.getLogger(UserJDBCDao.class);

    @Override
    public List<User> getUsers() {
        log.info("Start to getUsers from postgres via JDBC");

        List<User> users = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        log.debug("setup required attributes");

        try {
            String sql = "select * from users";
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            log.info("Connects to DB and execute the select query");

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String companyName = rs.getString("company_name");
                String address = rs.getString("address");
                String industry = rs.getString("industry");
                String managerName = rs.getString("manager_name");
                String title = rs.getString("title");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String type = rs.getString("type");
                log.info("Get all user attributes and translate to java object " + id);

                User user = new User();
                user.setUserId(id);
                user.setCompanyName(companyName);
                user.setAddress(address);
                user.setIndustry(industry);
                user.setManagerName(managerName);
                user.setTitle(title);
                user.setEmail(email);
                user.setPhone(phone);
                user.setType(type);
                users.add(user);
            }
        } catch (SQLException e) {
            log.error("Unable to connect to db or execute select all query", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                log.error("Unable to close the JDBC Connection",e);
            }
        }
        return users;
    }

    @Override
    public List<User> getSuppliers() {
        return null;
    }

    @Override
    public List<User> getOEMs() {
        return null;
    }

    @Override
    public boolean save(User user) {
        log.info("Start to create user in postgres via JDBC");
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        log.info("Setup required attributes");

        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO users (company_name, address, industry, manager_name, title, email, phone, type, product_id) VALUES (?,?,?,?,?,?,?,?,?)";
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            log.info("Connects to DB and execute the insert query");
            stmt.setString(1, user.getCompanyName());
            stmt.setString(2, user.getAddress());
            stmt.setString(3,user.getIndustry());
            stmt.setString(4, user.getManagerName());
            stmt.setString(5, user.getTitle());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.getPhone());
            stmt.setString(8, user.getType());
            stmt.setLong(9,1);
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                long generatedId = rs.getLong(1);
                user.setUserId(generatedId);
                return true;
            }
        } catch (SQLException e){
            log.error("Unable to connect to db or execute insert query", e);
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
    public User getById(long id) {
        return null;
    }

    @Override
    public List<User> getUsersByIndustry(String industry) {
        return null;
    }

    @Override
    public void updateCompanyName(long id, String name) {

    }

    @Override
    public void updateAddress(long id, String address) {

    }

    @Override
    public void updateIndustry(long id, String industry) {

    }

    @Override
    public void updateManager(long id, String manager, String title, String email, String phone) {

    }

    @Override
    public void delete(long id) {
        log.info("Start to delete User in postgres via JDBC");
        Connection con = null;
        PreparedStatement ps = null;
        int rowDeleted;
        log.debug("Setup required attributes");

        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "DELETE FROM users WHERE user_id = ?";
            ps = con.prepareStatement(sql);
            log.info("Connects to DB and execute the delete query");
            ps.setLong(1, id);
            rowDeleted = ps.executeUpdate();
            if(rowDeleted > 0)
                log.info("User {} deleted successfully.", id);
            else
                log.info("Failed to delete user {}", id);
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
