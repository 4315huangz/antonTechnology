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
        log.info("Start to getProduct from postgres via JDBC");

        List<Product> products = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        log.debug("setup required attribute");

        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT * FROM products";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            log.info("Connects to DB and execute the query");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int user_id = rs.getInt("user_id");
                log.info("Get all attributes and translate to java" + id);

                Product product = new Product();
                product.setId(id);
                product.setName(name);
                product.setDescription(description);
                product.setUser_id(user_id);
                products.add(product);
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
        return products;
    }

    @Override
    public List<Product> getById(int id) throws SQLException {
        return null;
    }

    @Override
    public boolean save(Product product) {
        return false;
    }

    @Override
    public Product updateName(int id, String name) {
        return null;
    }

    @Override
    public void delete(int id) {

    }
}
