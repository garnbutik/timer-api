package com.garnbutik.security;

import com.garnbutik.configuration.Configuration;
import com.garnbutik.exceptions.LoginException;
import com.garnbutik.model.User;
import com.garnbutik.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.Key;

@ApplicationScoped
public class JwtTokenParser implements TokenParser {

    @Inject
    private Configuration configuration;

    @Inject
    private UserService userService;

    @Override
    public User parseToken(String token) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(configuration.getStringProperty("authentication.jwt.secret")));
        try {
            Jws<Claims> list = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            String username = list.getBody().getSubject();
            User user = userService.getUserByUsernameOrEmail(username);
            if (user == null) {
                throw new LoginException("Token not valid");
            }
            return user;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new LoginException("Could not parse token, please check your token and try again.\n" +
                    e.getLocalizedMessage());
        }
    }
}
