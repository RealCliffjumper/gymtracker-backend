package com.antonk.gymtracker.JWT;

import com.antonk.gymtracker.entity.User;
import com.antonk.gymtracker.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JWTTokenProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600 * 1000);

        return JWT.create()
                .withIssuer(user.getUserLoginId())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("firstName", user.getUserFirstName())
                .withClaim("lastName", user.getUserLastName())
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT jwt = verifier.verify(token);

        User user = User.builder()
                .userLoginId(jwt.getIssuer())
                .userFirstName(jwt.getClaim("firstName").asString())
                .userLastName(jwt.getClaim("lastName").asString())
                .build();
        return  new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }
}
