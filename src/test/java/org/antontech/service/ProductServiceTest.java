package org.antontech.service;

import org.antontech.ApplicationBootstrap;
import org.antontech.dto.ProductDTO;
import org.antontech.dto.ProductDTOMapper;
import org.antontech.dto.UserDTO;
import org.antontech.model.Product;
import org.antontech.repository.ProductHibernateDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class ProductServiceTest {
    @Mock
    private ProductHibernateDao mockProductDao;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private ProductDTOMapper mockProductDTOMapper;
    @InjectMocks
    private ProductService productService;

    private Product product1;
    private ProductDTO productDTO;

    @Before
    public void setup() {
        product1 = new Product();
        product1.setId(1);
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(10.5);
        productDTO = new ProductDTO();
        productDTO.setId(1);
        productDTO.setName("Product 1");
        productDTO.setDescription("Description 1");
        productDTO.setIndustry("Auto");
        productDTO.setCompany("Ford");
    }

    @Test
    public void getProductsTest() {
        List<Product> products = new ArrayList<>();
        products.add(product1);
        when(mockProductDao.getProducts()).thenReturn(products);
        when(mockProductDTOMapper.apply(product1)).thenReturn(productDTO);

        List<ProductDTO> productDTOs = productService.getProducts();
        assertEquals(1, productDTOs.size());
    }

    @Test
    public void getByIdTest() {
        when(mockProductDao.getById(anyLong())).thenReturn(product1);
        when(mockProductDTOMapper.apply(product1)).thenReturn(productDTO);
        ProductDTO actual = productService.getById(1);
        assertNotNull(actual);
        assertEquals(productDTO, actual);
    }

    @Test
    public void saveTest() {
        productService.save(product1);
        verify(mockProductDao, times(1)).save(product1);
    }

    @Test
    public void updateNameTest() {
        productService.updateName(anyLong(), anyString());
        verify(mockProductDao, times(1)).updateName(anyLong(), anyString());
    }

    @Test
    public void updateDescriptionTest() {
        productService.updateDescription(anyLong(), anyString());
        verify(mockProductDao,times(1)).updateDescription(anyLong(), anyString());
    }

    @Test
    public void deleteTest() {
        productService.delete(anyLong());
        verify(mockProductDao, times(1)).delete(anyLong());
    }

    @Test
    public void searchByDescriptionTest() {
        List<Product> products = new ArrayList<>();
        products.add(product1);
        when(mockProductDao.searchByDescription(anyString())).thenReturn(products);
        when(mockProductDTOMapper.apply(product1)).thenReturn(productDTO);
        List<ProductDTO> productDTOs = productService.searchByDescription(anyString());
        assertEquals(productDTO, productDTOs.get(0));
    }

    @Test
    public void notifyAntonTechnologyTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setName("User 1");
        userDTO.setEmail("user@example.com");
        userDTO.setCompany("User Company");
        userDTO.setIndustry("IT");
        userDTO.setTitle("Manager");
        userDTO.setType("OEM");
        productService.notifyAntonTechnology(productDTO, userDTO);
        verify(mockEmailService, times(1)).notifyAntonTechnology(anyString(), anyString());
    }
}
