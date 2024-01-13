package org.antontech.service;

import org.antontech.ApplicationBootstrap;
import org.antontech.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class JWTServiceTest {
    @Autowired
    private JWTService jwtService;

    @Test
    public void generateTokenTest() {
        Account account = new Account();
        account.setId(1);
        account.setName("Ziwei");
        String token = jwtService.generateToken(account);

        String[] array = token.split("\\.");
        boolean bool = array.length == 3 ? true:false;
        assertTrue(bool);
    }
}
