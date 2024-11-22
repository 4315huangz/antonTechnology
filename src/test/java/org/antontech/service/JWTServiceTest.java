package org.antontech.service;

import io.jsonwebtoken.Claims;
import org.antontech.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class JWTServiceTest {
    @InjectMocks
    private JWTService jwtService;

    @Test
    public void generateTokenTest() {
        User user = new User();
        user.setUserId(1);
        user.setUserName("z4315");
        String token = jwtService.generateToken(user);

        String[] array = token.split("\\.");
        boolean bool = array.length == 3 ? true:false;
        assertTrue(bool);
    }

    @Test
    public void decryptTokenTest() {
        User expectedUser = new User();
        expectedUser.setUserId(1);
        expectedUser.setUserName("z4315");

        String token = jwtService.generateToken(expectedUser);

        Claims decryptedClaims = jwtService.decryptToken(token);

        assertEquals(String.valueOf(expectedUser.getUserId()), decryptedClaims.getId());
        assertEquals(expectedUser.getUserName(), decryptedClaims.getSubject());
    }
}
