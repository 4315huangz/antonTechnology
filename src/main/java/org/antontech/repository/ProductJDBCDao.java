package org.antontech.repository;

import org.antontech.model.Product;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductJDBCDao implements IProductDao {
    private static final String DB_URL = "jdbc:postgresql://localhost:5430/antontech_db";
    private static final String USER = "admin";
    private static final String PASS = "ziwei123!";
    private static final Logger log = LoggerFactory.getLogger(ProductJDBCDao.class);


    public List<Product> getProducts() {
        log.info("Start to getProducts from postgres via JDBC");

        List<Product> products = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        log.debug("setup required attributes");

        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT * FROM products";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            log.info("Connects to DB and execute the select query");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                log.info("Get all product attributes and translate to java object " + id);

                Product product = new Product();
                product.setId(id);
                product.setName(name);
                product.setDescription(description);
                products.add(product);
            }
        } catch (SQLException e){
            log.error("Unable to connect to db or execute select query", e);
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
        return products;
    }

    @Override
    public boolean save(Product product) {
        log.info("Start to create Product in postgres via JDBC");
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        log.info("Setup required attributes");

        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO products (name, description, user_id) VALUES (?,?,?)";
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            log.info("Connects to DB and execute the insert query");
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setLong(3,0);
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                long generatedId = rs.getLong(1);
                product.setId(generatedId);
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
    public Product getById(long id) {
        return null;
    }

    @Override
    public void updateName(long id, String name) {

    }

    @Override
    public void updateDescription(long id, String description) {

    }

    @Override
    public void delete(long id) {
        log.info("Start to delete Product in postgres via JDBC");
        Connection con = null;
        PreparedStatement ps = null;
        int rowDeleted;
        log.debug("Setup required attributes");

        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "DELETE FROM products WHERE id = ?";
            ps = con.prepareStatement(sql);
            log.info("Connects to DB and execute the delete query");
            ps.setLong(1, id);
            rowDeleted = ps.executeUpdate();
            if(rowDeleted > 0)
                log.info("Product {} deleted successfully.", id);
            else
                log.info("Failed to delete product {}", id);
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
    public List<Product> searchByDescriptionKeyword(String keyword) {
        log.info("Start to search Product through description from postgres via JDBC");

        List<Product> products = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        log.debug("setup required attributes");

        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT * FROM products WHERE LOWER(description) LIKE ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1,"%" + keyword.toLowerCase() + "%");
            rs = stmt.executeQuery();
            log.info("Connects to DB and execute the select query");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                log.info("Get all product attributes and translate to java object " + id);

                Product product = new Product();
                product.setId(id);
                product.setName(name);
                product.setDescription(description);
                products.add(product);
            }
        } catch (SQLException e){
            log.error("Unable to connect to db or execute select query", e);
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
        return products;
    }

}
