package com.garnbutik.security;

import com.garnbutik.configuration.Configuration;
import com.garnbutik.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.mapstruct.Named;
import org.mapstruct.Qualifier;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.security.Key;
import java.util.Date;

@ApplicationScoped
public class JwtTokenIssuer implements TokenIssuer {

    @Inject
    private Configuration configuration;

    @Override
    @Named("issueToken")
    public String issueToken(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new BadRequestException("No username provided");
        }
        long nowMillis = System.currentTimeMillis();
        Date issuedAt = new Date(nowMillis);

        long thirtyMinutes = System.currentTimeMillis() + (configuration.getLongProperty("authentication.jwt.defaultExpirationTime"));
//                configuration != null
//                ? configuration.getLongProperty("authentication.jwt.defaultExpirationTime")
//                        : 500000);
        Date expiration = new Date(thirtyMinutes);

        String jws = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setIssuer("Timer")
                .signWith(createKey())
                .setExpiration(expiration)
                .compact();
        return jws;
    }

    private Key createKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(configuration.getStringProperty("authentication.jwt.secret")));
//                configuration != null
//                        ? configuration.getStringProperty("authentication.jwt.secret")
//                        : "aKeyWhichIsJibberishAndaKeyWhichIsJibberishAgain"));
    }
}
