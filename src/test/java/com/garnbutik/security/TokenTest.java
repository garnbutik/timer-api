package com.garnbutik.security;

import com.garnbutik.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;


import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import static org.junit.Assert.*;

@RunWith(WeldJUnit4Runner.class)
public class TokenTest {

    @Inject
    JwtTokenIssuer tokenIssuer;

    @Inject
    Logger logger;

    @Test
    public void tokenShouldBe152Chars(){
       String token = tokenIssuer.issueToken(createUserWithUsername());
       logger.info(String.valueOf(token.length()));
       assertEquals(152, token.length());
    }

    @Test
    public void tokenShouldStartWithEy() {
        String token = tokenIssuer.issueToken(createUserWithUsername());
        assertEquals("ey", token.substring(0, 2).toLowerCase());
    }

    @Test
    public void shouldIssueToken(){
        String token = tokenIssuer.issueToken(createUserWithUsername());
        assertNotNull(token);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowExceptionWhenUserDoesntHaveUsername(){
        String token = tokenIssuer.issueToken(new User());

    }

    private User createUserWithUsername(){
        User user = new User();
        user.setUsername("Fredrik");
        return user;
    }
}
