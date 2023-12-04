package org.antontech.repository;

import org.antontech.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface IProductDao {
    List<Product> getProducts() throws SQLException;
}
