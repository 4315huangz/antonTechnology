package org.antontech.controller;

import org.antontech.model.Product;
import org.antontech.model.User;
import org.antontech.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/oem", method = RequestMethod.POST)
    public List<Product> getProductsByOEM(@RequestBody User user) {
        logger.info("Pass is variable user {}", user);
        return productService.getProductsByOEM(user);
    }

    @RequestMapping(value = "/oem/{id}", method = RequestMethod.GET)
    public Product getProductByIdByOEM(User user, @PathVariable long id) {
        logger.info("I am in getProductByIdByOEM controller");
        return productService.getProductByIdByOEM(user, id);
    }

    @RequestMapping(value = "/oem/{keyword}", method = RequestMethod.GET)
    public List<Product> searchProductsByOEM(User user, @PathVariable String keyword) {
        logger.info("I am in searchProductsByOEM controller");
        return productService.searchProductsByOEM(user, keyword);
    }

    /*
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void saveProductsBySupplier(User user, Product product) {
        logger.info("I am in saveProductsBySupplier controller");
        productService.saveProductsBySupplier(user, product);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void updateNameBySupplier(User user, String name) {
        logger.info("I am in updateNameBySupplier controller");
        productService.updateNameBySupplier(user, id, name);
    }
    */


}
