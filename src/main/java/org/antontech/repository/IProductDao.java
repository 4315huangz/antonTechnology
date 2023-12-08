package org.antontech.repository;

import org.antontech.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface IProductDao {
    List<Product> getProducts() throws SQLException;

    //Create product
    boolean save(Product product);

    //Search product by id
    Product getById(int id);

    //Search product by Industry
    List<Product> getByIndustry(String industry);

    //Update product name
    Product updateName(int id, String name);

    //Update product description
    void updateDescription(int id, String description);

    //Delete product
    void delete(int id);
}
