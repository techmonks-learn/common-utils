package com.tm.common.hashing;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 *   Title: SHA256
 * FileName: SHA256.java
 *   @version 1.0
 *   @Created date: 28-11-2017
 *   @author Next Sphere Technologies
 *   Copyright: Copyright © Next Sphere Technologies 2017.
 */
public class SHA256 implements HashProvider {

    @Override
    public String generateHash(String text, String salt) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        if (!StringUtils.isEmpty(salt)) {
            digest.update(salt.getBytes("UTF-8"));
        }
        byte[] hash = digest.digest(
                text.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(hash));
    }
}
