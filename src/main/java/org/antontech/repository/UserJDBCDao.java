package org.antontech.repository;

import org.antontech.model.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserJDBCDao implements IUserDao{
    private static final String DB_URL = System.getProperty("database.url");
    private static final String USER = System.getProperty("database.user");
    private static final String PASS = System.getProperty("database.password");
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
                user.setPassword(rs.getString("password"));
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
                if (con != null) con.close();
            } catch (SQLException e) {
                log.error("Unable to close the JDBC Connection",e);
            }
        }
        return user;
    }

    @Override
    public List<User> getByIndustry(String industry) {
        log.info("Start to get user by industry from postgres via JDBC");
        List<User>  users = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        log.debug("setup required attributes");

        try {
            String sql = "select * from users WHERE LOWER(industry) = LOWER(?)";
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = con.prepareStatement(sql);
            stmt.setString(1, industry);
            rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
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
                users.add(user);
            }
            log.info("Connects to DB and execute the select query successfully");
        } catch (SQLException e) {
            log.error("Unable to connect to db or execute select all query", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                log.error("Unable to close the JDBC Connection",e);
            }
        }
        return users;
    }

    @Override
    public void updateEmail(long id, String email) {
        log.info("Start to update user email by ID via JDBC");
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "UPDATE users SET email = ? WHERE user_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setLong(2, id);
                int rowAffected = stmt.executeUpdate();
                if (rowAffected > 0) {
                    log.info("Successfully updated user email for ID {}", id);
                } else {
                    log.warn("No user found with ID {}", id);
                }
            }
        } catch (SQLException e){
            log.error("Unable to connect to db or execute update", e);
        }
    }

    @Override
    public void updatePassword(long id, String password) {
        log.info("Start to update user password by ID via JDBC");
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "UPDATE users SET password = ? WHERE user_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                String hashedPassword = DigestUtils.md5Hex(password.trim());
                stmt.setString(1, hashedPassword);
                stmt.setLong(2, id);
                int rowAffected = stmt.executeUpdate();
                if (rowAffected > 0) {
                    log.info("Successfully updated user password for ID {}", id);
                } else {
                    log.warn("No user found with ID {}", id);
                }
            }
        } catch (SQLException e){
            log.error("Unable to connect to db or execute update", e);
        }
    }

    @Override
    public void updateCompany(long id, String companyName, String address, String industry) {
        log.info("Start to update user company by ID via JDBC");
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "UPDATE users SET company_name = ?, address = ?, industry = ?  WHERE user_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, companyName);
                stmt.setString(2, address);
                stmt.setString(3, industry);
                stmt.setLong(4, id);
                int rowAffected = stmt.executeUpdate();
                if (rowAffected > 0) {
                    log.info("Successfully updated user company for ID {}", id);
                } else {
                    log.warn("No user found with ID {}", id);
                }
            }
        } catch (SQLException e){
            log.error("Unable to connect to db or execute update", e);
        }
    }

    @Override
    public void updateManager(long id, String firstName, String lastName, String title, String phone) {
        log.info("Start to update user manager by ID via JDBC");
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "UPDATE users SET first_name = ?, last_name = ?, title = ?, phone = ?  WHERE user_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, title);
                stmt.setString(4, phone);
                stmt.setLong(5, id);
                int rowAffected = stmt.executeUpdate();
                if (rowAffected > 0) {
                    log.info("Successfully updated user manager for ID {}", id);
                } else {
                    log.warn("No user found with ID {}", id);
                }
            }
        } catch (SQLException e){
            log.error("Unable to connect to db or execute update", e);
        }
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
