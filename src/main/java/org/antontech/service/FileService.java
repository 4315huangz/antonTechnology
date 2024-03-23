package org.antontech.service;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.antontech.service.exception.InvalidFileTypeException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class FileService {
    private String bucketName = "ascending-training-ziwei";
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    AmazonS3 s3Client;

    public void uploadFile(File file) throws IOException {
        s3Client.putObject(bucketName, file.getName(), file);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if(file == null) {
            logger.error("Cannot upload a null file");
            throw new InvalidFileTypeException("Cannot upload a null file");
        }
        String uuid = UUID.randomUUID().toString();
        String originalFileName = file.getOriginalFilename();
        String newFileName = FilenameUtils.removeExtension(originalFileName) + uuid + "." + FilenameUtils.getExtension(originalFileName);
        logger.info("The new file name is created: {}", newFileName);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        PutObjectRequest request = new PutObjectRequest(bucketName, newFileName, file.getInputStream(), objectMetadata);
        s3Client.putObject(request);
        return s3Client.getUrl(bucketName, file.getName()).toString();
    }

    public void downloadFile(String fileUrl, String destinationDirectory) throws IOException {
        int lastSlashIndex = fileUrl.lastIndexOf('/');
        String fileName = fileUrl.substring(lastSlashIndex + 1);
        logger.info("Construct the file name for file to be downloaded: {}", fileName);

        Path directoryPath = Paths.get(destinationDirectory);
        if(!Files.exists(directoryPath))
            Files.createDirectories(directoryPath);
        if (!Files.isDirectory(directoryPath)) {
            throw new IllegalArgumentException("Destination is not a directory: " + destinationDirectory);
        }
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        InputStream inputStream = s3Object.getObjectContent();
        File file = new File(destinationDirectory, fileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        FileCopyUtils.copy(inputStream, outputStream);
        outputStream.close();
    }

    public void deleteFile(String fileUrl) {
        int lastSlashIndex = fileUrl.lastIndexOf('/');
        String key = fileUrl.substring(lastSlashIndex + 1);
        logger.info("Construct the key for file to be deleted: {}", key);
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
    }
}
