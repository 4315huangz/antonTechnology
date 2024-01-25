package org.antontech.controller;

import org.antontech.model.User;
import org.antontech.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = {"/user","/users"})
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<User> getUsers() {
        logger.info("I am in getUsers controller");
        List<User> users = userService.getUsers();
        return users;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody User user) {
        logger.info("Post a new user object {}", user.getUserId());
        boolean isSaved = userService.save(user);
        if (isSaved)
            return ResponseEntity.status(HttpStatus.CREATED).body("User is created successfully");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create user");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getUserById(@PathVariable(name = "id") long id) {
        User u = userService.getById(id);
        if (u == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + id + " not found");
        return ResponseEntity.ok(u);
    }

    @RequestMapping(value = "/search/{industry}", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getByIndustry(@PathVariable(name = "industry") String industry) {
        logger.info("Pass in variable industry {}.", industry);
        List<User> users = userService.getByIndustry(industry.toLowerCase().trim());
        if (users == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/email/{id}", params = {"email"}, method = RequestMethod.PATCH)
    public ResponseEntity updateEmail(@PathVariable(name = "id") long id, @RequestParam(name = "email") String email) {
        logger.info("Pass in variable id: {} and email {}.", id, email);
        userService.updateEmail(id, email);
        return ResponseEntity.ok().body("Email for user "+id+" is updated successfully");
    }

    @RequestMapping(value = "/password/{id}", params = {"password"},method = RequestMethod.PATCH)
    public ResponseEntity updatePassword(@PathVariable(name = "id")long id, @RequestParam(name = "password") String password) {
        String encryptedPassword = DigestUtils.md5Hex(password.trim());
        logger.info("Pass in variable id: {} and password {}.", id, encryptedPassword);
        userService.updatePassword(id, encryptedPassword);
        return ResponseEntity.ok().body("Password for user " + id + " is updated successfully");
    }

    @RequestMapping(value = "/company/{id}",params = {"company","address","industry"}, method = RequestMethod.PATCH)
    public ResponseEntity updateCompany(@PathVariable(name = "id")long id, @RequestParam(name = "company") String companyName,
                                        @RequestParam(name = "address") String address, @RequestParam(name = "industry") String industry) {
        logger.info("Pass in variable id: {} and company {}.", id, companyName);
        userService.updateCompany(id, companyName, address, industry);
        return ResponseEntity.ok().body("Company for user " + id + " is updated successfully");
    }

    @RequestMapping(value = "/manager/{id}", params = {"firstName","lastName","title","phone"}, method = RequestMethod.PATCH)
    public ResponseEntity updateManager(@PathVariable(name = "id")long id, @RequestParam(name = "firstName")String firstName,
                                        @RequestParam(name = "lastName")String lastName, @RequestParam(name = "title")String title,
                                        @RequestParam(name = "phone")String phone) {
        logger.info("Pass in variable id: {}, firstNmae {}, lastName{}.", id, firstName,lastName);
        userService.updateManager(id, firstName, lastName, title, phone);
        return ResponseEntity.ok().body("Manager for user " + id + " is updated successfully");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable(name = "id") long id) {
        logger.info("I am in delete User controller");
        userService.delete(id);
        return ResponseEntity.ok().body("User " + id + " is deleted successfully");
    }

}
