package org.antontech.repository;

import org.antontech.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface IProductDao {
    List<Product> getProducts() throws SQLException;

    //Retrieve
    List<Product> getById(int id) throws SQLException;

    //Create
    boolean save(Product product);

    //Update
    Product updateName(int id, String name);

    //Delete
    void delete(int id);
}
