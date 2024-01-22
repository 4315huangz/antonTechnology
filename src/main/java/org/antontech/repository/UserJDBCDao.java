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
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String companyName = rs.getString("company_name");
                String address = rs.getString("address");
                String industry = rs.getString("industry");
                String title = rs.getString("title");
                String phone = rs.getString("phone");
                String companyType = rs.getString("company_type");
                log.info("Get all user attributes and translate to java object " + id);

                User user = new User();
                user.setUserId(id);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setCompanyName(companyName);
                user.setAddress(address);
                user.setIndustry(industry);
                user.setTitle(title);
                user.setPhone(phone);
                user.setCompanyType(companyType);
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
    public boolean save(User user) {
        log.info("Start to create user in postgres via JDBC");
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        log.info("Setup required attributes");

        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO users (user_name, password, first_name, last_name, email, company_name, address, industry, title, phone, company_type) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            log.info("Connects to DB and execute the insert query");
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3,user.getFirstName());
            stmt.setString(4,user.getLastName());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getCompanyName());
            stmt.setString(7, user.getAddress());
            stmt.setString(8, user.getIndustry());
            stmt.setString(9, user.getTitle());
            stmt.setString(10, user.getPhone());
            stmt.setString(11, user.getCompanyType());
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
        log.info("Start to get user by id from postgres via JDBC");
        User user = new User();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        log.debug("setup required attributes");

        try {
            String sql = "select * from users where user_id = ?";
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                user.setUserId(rs.getLong("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setCompanyName(rs.getString("company_name"));
                user.setAddress(rs.getString("address"));
                user.setIndustry(rs.getString("industry"));
                user.setTitle(rs.getString("title"));
                user.setPhone(rs.getString("phone"));
                user.setCompanyType(rs.getString("company_type"));
            }
            log.info("Connects to DB and execute the select query successfully");
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
        return user;
    }

    @Override
    public List<User> getByIndustry(String industry) {
        return null;
    }

    @Override
    public void updateEmail(long id, String email) {

    }

    @Override
    public void updatePassword(long id, String password) {

    }

    @Override
    public void updateCompany(long id, String companyName, String address, String industry) {

    }

    @Override
    public void updateManager(long id, String firstName, String lastName, String title, String phone) {

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

    @Override
    public User getUserByCredentials(String email, String password) throws Exception {
        return null;
    }
}
