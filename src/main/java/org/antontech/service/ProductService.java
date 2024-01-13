package org.antontech.service;

import org.antontech.controller.ProductController;
import org.antontech.model.Product;
import org.antontech.model.User;
import org.antontech.repository.IProductDao;
import org.antontech.repository.IUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private IProductDao productDao;

    public List<Product> getProducts () {
        return productDao.getProducts();
    }

    public List<Product> getProductsByOEM (User user) {
        if("OEM".equals(user.getType())) {
            return productDao.getProducts();
        } else {
            logger.warn("Unexpected user type: {}", user.getType());
            return List.of();
        }
    }

    public Product getProductByIdByOEM (User user, long id) {
        if("OEM".equals(user.getType())) {
            return productDao.getById(id);
        } else {
            logger.warn("Unexpected user type: {}", user.getType());
            return null;
        }
    }

    public List<Product> searchProductsByOEM (User user, String keyword) {
        if("OEM".equals(user.getType())) {
            return productDao.searchByDescriptionKeyword(keyword);
        } else {
            logger.warn("Unexpected user type: {}", user.getType());
            return List.of();
        }
    }

    public void saveProductsBySupplier(User user, Product product) {
        if("Supplier".equals(user.getType())) {
            productDao.save(product);
        } else {
            logger.warn("Unexpected user type: {}", user.getType());
        }
    }

    public void updateNameBySupplier(User user, long id, String name) {
        if("Supplier".equals(user.getType())) {
            productDao.updateName(id,name);
        } else {
            logger.warn("Unexpected user type: {}", user.getType());
        }
    }

    public void updateDescriptionBySupplier(User user, long id, String des) {
        if("Supplier".equals(user.getType())) {
            productDao.updateDescription(id,des);
        } else {
            logger.warn("Unexpected user type: {}", user.getType());
        }
    }

    public void deleteBySupplier(User user, long id) {
        if("Supplier".equals(user.getType())) {
            productDao.delete(id);
        } else {
            logger.warn("Unexpected user type: {}", user.getType());
        }
    }
}
