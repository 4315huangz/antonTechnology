package org.antontech.controller;

import org.antontech.service.FileService;
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

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity delete(@RequestParam(name = "url") String fileUrl) {
        try{
            fileService.deleteFile(fileUrl);
            logger.info("Successfully deleted file from S3. File URL: {}", fileUrl);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Unable to delete file from AWS S3, error = {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
