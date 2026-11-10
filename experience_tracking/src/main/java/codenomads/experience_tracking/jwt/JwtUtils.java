package codenomads.experience_tracking.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import codenomads.experience_tracking.exceptions.CustomException;

import java.util.Calendar;
import java.util.Date;


@Component
public class JwtUtils {

    @Value("${jwt.key}")
    private String key;

    @Value("${jwt.expire}")
    private int expire;


    // Validate the token with the user
    public boolean validateToken(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(key)).build();
        try {
            DecodedJWT verify = jwtVerifier.verify(token);
            if (verify.getExpiresAt() != null && verify.getExpiresAt().before(new Date())) {
                return false;
            }
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public Long extractUserId(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(key)).build().verify(token);
            return decodedJWT.getClaim("id").asLong();
        } catch (JWTVerificationException e) {
            throw new CustomException("Invalid JWT token", 404);
        }
    }
}
