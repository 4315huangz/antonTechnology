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
        log.debug("setup required attribute");

        try {
            String sql = "select * from users";
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            log.info("Get all the attribute and translate to Java");

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String company_name = rs.getString("company_name");
                String address = rs.getString("address");
                String industry = rs.getString("industry");
                String manager_name = rs.getString("manager_name");
                String title = rs.getString("title");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String type = rs.getString("type");
                int product_id = rs.getInt("product_id");

                User user = new User();
                user.setUser_id(id);
                user.setCompany_name(company_name);
                user.setAddress(address);
                user.setIndustry(industry);
                user.setManager_name(manager_name);
                user.setTitle(title);
                user.setEmail(email);
                user.setPhone(phone);
                user.setType(type);
                users.add(user);
            }
        } catch (SQLException e) {
            log.error("Unable to connect to db or execute query", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {

            }
        }
        return users;
    }

    @Override
    public boolean save(User user) {
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

    }
}
