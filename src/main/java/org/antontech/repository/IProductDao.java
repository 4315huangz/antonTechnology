package org.antontech.repository;

import org.antontech.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface IProductDao {
    List<Product> getProducts();

    boolean save(Product product);

    Product getById(long id);

    void updateName(long id, String name);

    void updateDescription(long id, String description);

    void delete(long id);
}
