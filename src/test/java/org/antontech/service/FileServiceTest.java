package org.antontech.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.antontech.ApplicationBootstrap;
import org.antontech.service.exception.ResourceNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {
    @InjectMocks
    FileService fileService;

    @Mock
    private File mockFile;

    @Mock
    private AmazonS3 mockS3Client;

    @Mock
    private AmazonS3Exception mockS3Exception;

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

    @Test
    public void deleteFileTest_FileExists_DeleteSuccessfully() {
        String fileUrl = "https://bucket-name.s3.amazonaws.com/testFile.txt";
        String key = "testFile.txt";

        when(mockS3Client.doesObjectExist(anyString(), eq(key))).thenReturn(true);

        fileService.deleteFile(fileUrl);

        verify(mockS3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    public void deleteFileTest_FileDoesNotExist_DeleteFailed() {
        String fileUrl = "https://bucket-name.s3.amazonaws.com/testFile.txt";
        String key = "testFile.txt";

        when(mockS3Client.doesObjectExist(anyString(), eq(key))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                fileService.deleteFile(fileUrl)
        );

        verify(mockS3Client, never()).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    public void deleteFileTest_S3Exception() {
        String fileUrl = "https://bucket-name.s3.amazonaws.com/testFile.txt";
        String key = "testFile.txt";

        when(mockS3Client.doesObjectExist(anyString(), eq(key))).thenReturn(true);
        doThrow(mockS3Exception).when(mockS3Client).deleteObject(any(DeleteObjectRequest.class));

        AmazonS3Exception exception = assertThrows(AmazonS3Exception.class, () ->
                fileService.deleteFile(fileUrl)
        );

        assertEquals(mockS3Exception.getMessage(), exception.getMessage());
        verify(mockS3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }
}
