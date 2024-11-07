package org.antontech.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.antontech.ApplicationBootstrap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class FileServiceTest {
    @Autowired
    FileService fileService;

    @Mock
    private File mockFile;

    @Autowired
    private AmazonS3 mockS3Client;

    @Test
    public void uploadFIleTest() throws IOException {
        when(mockFile.getName()).thenReturn("testFile");
        fileService.uploadFile(mockFile);
        verify(mockS3Client, times(1))
                .putObject(anyString(),anyString(),any(File.class));
    }

    @Test
    public void uploadMultipartFIleTest() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "fileName", "fileName", "text/plain", "Hello, World!".getBytes());
        String expectedUrl = "https://example.com";
        when(mockS3Client.getUrl(anyString(), anyString())).thenReturn(new URL(expectedUrl));

        String actualUrl = fileService.uploadFile(mockMultipartFile);

        verify(mockS3Client).putObject(any(PutObjectRequest.class));
        assertEquals(expectedUrl, actualUrl);
    }

}
