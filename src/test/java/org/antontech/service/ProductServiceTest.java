package org.antontech.service;

import org.antontech.dto.ProductDTO;
import org.antontech.dto.ProductDTOMapper;
import org.antontech.dto.UserDTO;
import org.antontech.model.Product;
import org.antontech.repository.ProductHibernateDao;
import org.antontech.service.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
    @Mock
    private ProductHibernateDao mockProductDao;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private ProductDTOMapper mockProductDTOMapper;
    @Mock
    private FileService mockFileService;
    @Mock
    private MultipartFile mockFile;
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
    public void uploadProductSamplePictureTest() throws IOException {
        String pictureUrl = "https://bucket-name.s3.amazonaws.com/samplePicture.jpg";

        when(mockFileService.uploadFile(mockFile)).thenReturn(pictureUrl);
        when(mockProductDao.getById(anyLong())).thenReturn(product1);

        productService.uploadProductSamplePicture(product1.getId(), mockFile);

        verify(mockProductDao, times(1)).savePictureUrl(product1.getId(), pictureUrl);
    }

    @Test
    public void uploadProductSamplePicture_ProductNotFound() throws IOException {
        String pictureUrl = "https://bucket-name.s3.amazonaws.com/samplePicture.jpg";

        when(mockFileService.uploadFile(mockFile)).thenReturn(pictureUrl);
        when(mockProductDao.getById(anyLong())).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.uploadProductSamplePicture(anyLong(), mockFile);
        });

        verify(mockProductDao, never()).savePictureUrl(anyLong(), anyString());
    }

    @Test
    public void deleteProductSamplePictureTest_Success() throws IOException {
        when(mockProductDao.getById(anyLong())).thenReturn(product1);
        doNothing().when(mockProductDao).deletePictureUrl(product1.getId());

        productService.deleteProductSamplePicture(product1.getId());

        verify(mockProductDao, times(1)).deletePictureUrl(product1.getId());
    }

    @Test
    public void deleteProductSamplePicture_ProductNotFound() throws IOException {
        when(mockProductDao.getById(anyLong())).thenReturn(null);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProductSamplePicture(product1.getId());
        });

        assertEquals("Product with id " + product1.getId() + " not found", exception.getMessage());
        verify(mockProductDao, never()).deletePictureUrl(anyLong());
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
        String subject = "Consulting Service Requested";
        String emailContent = "The User: " + userDTO.getId() + "\n"
                + "Name: " + userDTO.getName()
                + "Email: " + userDTO.getEmail() + "\n"
                + "From Company: " + userDTO.getCompany() + "\n"
                + "has requested more details about the following product:\n\n"
                + "Product ID: " + productDTO.getId() + "\n"
                + "Product Name: " + productDTO.getName() + "\n"
                + "Description: " + productDTO.getDescription() + "\n"
                + "Company: " + productDTO.getCompany() + "\n"
                + "Industry: " + productDTO.getIndustry();

        productService.notifyAntonTechnology(productDTO, userDTO);

        verify(mockEmailService, times(1)).notifyAntonTechnology(subject, emailContent, userDTO.getEmail());
    }
}
