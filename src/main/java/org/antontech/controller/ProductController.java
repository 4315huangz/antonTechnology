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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Product> getProducts() {
        logger.info("I am in getProducts controller");
        List<Product> products = productService.getProducts();
        return products;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Product getProductByIdByOEM( @PathVariable long id) {
        logger.info("I am in getProductByIdByOEM controller");
        return productService.getById(id);
    }

    @RequestMapping(value = "/{keyword}", method = RequestMethod.GET)
    public List<Product> searchProductsByOEM(@PathVariable String keyword) {
        logger.info("I am in searchProductsByOEM controller");
        return productService.searchByDescription(keyword);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void saveProductsBySupplier( @RequestBody Product product) {
        logger.info("I am in saveProductsBySupplier controller");
        productService.save(product);
    }

}
