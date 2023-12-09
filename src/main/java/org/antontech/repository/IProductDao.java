package org.antontech.repository;

import org.antontech.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface IProductDao {
    List<Product> getProducts() throws SQLException;

    //Create product
    boolean save(Product product);

    //Search product by id
    Product getById(long id);

    //Update product name
    void updateName(long id, String name);

    //Update product description
    void updateDescription(long id, String description);

    //Delete product
    void delete(long id);
}
