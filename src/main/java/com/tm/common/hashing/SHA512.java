package com.tm.common.hashing;

import org.springframework.util.StringUtils;

import java.security.MessageDigest;

/**
 *   Title: SHA512
 * FileName: SHA512.java
 *   @version 1.0
 *   @Created date: 28-11-2017
 *   @author Next Sphere Technologies
 *   Copyright: Copyright © Next Sphere Technologies 2017.
 */
public class SHA512 implements HashProvider {
    @Override
    public String generateHash(String text, String salt) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        if (!StringUtils.isEmpty(salt)) {
            messageDigest.update(salt.getBytes("UTF-8"));
        }
        byte[] bytes = messageDigest.digest(text.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
