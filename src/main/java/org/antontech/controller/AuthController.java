package org.antontech.controller;

import org.antontech.model.Account;
import org.antontech.service.AccountService;
import org.antontech.service.JWTService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    JWTService jwtService;
    @Autowired
    AccountService accountService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity userLogin(@RequestBody Account account) {
        try {
            Account a = accountService.getAccountByCredentials(account.getEmail(), account.getPassword());
            if(a == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.ok().body(jwtService.generateToken(a));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }
}
