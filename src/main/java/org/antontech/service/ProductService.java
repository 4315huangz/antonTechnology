package org.antontech.service;

import org.antontech.dto.ProductDTO;
import org.antontech.dto.ProductDTOMapper;
import org.antontech.dto.UserDTO;
import org.antontech.model.Product;
import org.antontech.model.User;
import org.antontech.repository.IProductDao;
import org.antontech.service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private IProductDao productDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ProductDTOMapper productDTOMapper;
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<ProductDTO> getProducts() {
        return productDao.getProducts()
                .stream()
                .map(productDTOMapper)
                .collect(Collectors.toList());
    }

    public ProductDTO getById(long id) {
        Product p = productDao.getById(id);
        if(p != null)
            return productDTOMapper.apply(p);
        else
            throw new ResourceNotFoundException("Product with id " + id + " not found");
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

    public List<ProductDTO> searchByDescription(String keyword) {
        return productDao.searchByDescription(keyword)
                .stream().map(productDTOMapper)
                .collect(Collectors.toList());
    }

    public void notifyAntonTechnology(ProductDTO productDto, UserDTO user) {
        String subject = "Consulting Service Requested";
        String emailContent = "The User: " + user.getId() + "\n"
                + "Name: " + user.getName()
                + "Email: " + user.getEmail() + "\n"
                + "From Company: " + user.getCompany() + "\n"
                + "has requested more details about the following product:\n\n"
                + "Product ID: " + productDto.getId() + "\n"
                + "Product Name: " + productDto.getName() + "\n"
                + "Description: " + productDto.getDescription() + "\n"
                + "Company: " + productDto.getCompany() + "\n"
                + "Industry: " + productDto.getIndustry();
        emailService.notifyAntonTechnology(subject, emailContent);
    }
}
