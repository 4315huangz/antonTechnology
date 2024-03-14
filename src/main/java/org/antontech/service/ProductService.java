package org.antontech.service;

import org.antontech.model.Product;
import org.antontech.model.User;
import org.antontech.repository.IProductDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private IProductDao productDao;
    @Autowired
    private EmailService emailService;
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<Product> getProducts() {
        return productDao.getProducts();
    }

    public Product getById(long id) {
        return productDao.getById(id);
    }

    public boolean save(Product p) {
        return productDao.save(p);
    }

    public void updateName(long id, String name) {
        productDao.updateName(id, name);
    }

    public void updateDescription(long id, String description) {
        productDao.updateDescription(id, description);
    }

    public void delete(long id) {
        productDao.delete(id);
    }

    public List<Product> searchByDescription(String keyword) {
        return productDao.searchByDescription(keyword);
    }

    public void notifyAntonTechnology(Product product, User user) {
        String subject = "Consulting Service Requested";
        String emailContent = "User: " + user.getUserId() + user.getFirstName() + user.getLastName() + "\n"
                + "Email: " + user.getEmail() + "\n"
                + "has requested more details about the following product:\n\n"
                + "Product ID: " + product.getId() + "\n"
                + "Product Name: " + product.getName() + "\n"
                + "Description: " + product.getDescription() + "\n";
        emailService.notifyAntonTechnology(subject, emailContent);
    }
}
