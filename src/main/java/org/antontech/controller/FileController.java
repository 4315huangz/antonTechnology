package org.antontech.controller;

import org.antontech.service.FileService;
import org.antontech.service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InvalidClassException;

@RestController
@RequestMapping(value = "/files")
public class FileController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    FileService fileService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity upload(@RequestParam(name = "file") MultipartFile file) {
        try{
            String url = fileService.uploadFile(file);
            logger.info("Successfully upload file{} to S3", file.getName());
            return ResponseEntity.ok().body(url);
        } catch (InvalidClassException e) {
            logger.error("File is invalid");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IOException e) {
            logger.error("Fail to get file input stream");
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            logger.error("Unable to upload file to AWS S3, error = {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<String> download(@RequestParam(name = "fileUrl") String fileUrl,
                                   @RequestParam(name = "downloadDirectory") String downloadDirectory) {
        try {
            fileService.downloadFile(fileUrl, downloadDirectory);
            return ResponseEntity.ok("File downloaded successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Invalid destination directory: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid destination directory: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Error downloading file from AWS S3: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error downloading file from AWS S3: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<String> delete(@RequestParam(name = "url") String fileUrl) {
        try{
            fileService.deleteFile(fileUrl);
            logger.info("Successfully deleted file from S3. File URL: {}", fileUrl);
            return ResponseEntity.ok("File is deleted from S3 successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File does not exist in S3");
        } catch (Exception e) {
            logger.error("Unable to delete file from AWS S3, error = {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fail to delete the file from S3");
        }
    }
}
