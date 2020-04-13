package com.tm.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class JwtUtility {
    public JwtUtility() {
    }

    public static String createJWT(String id, String issuer, String subject, long ttlMillis, Map<String, Object> additionalClaims, String secretKey) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).setIssuer(issuer).setHeaderParam("typ", "jwt");
        if (ttlMillis > 0L) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        if (Objects.nonNull(additionalClaims) && !additionalClaims.isEmpty()) {
            additionalClaims.forEach((key, value) -> {
                builder.claim(key, value);
            });
        }

        return builder.signWith(signatureAlgorithm, signingKey).compact();
    }

    public static Claims decodeJWT(String jwt, String secretKey) {
        Claims claims = (Claims)Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)).parseClaimsJws(jwt).getBody();
        return claims;
    }
}
