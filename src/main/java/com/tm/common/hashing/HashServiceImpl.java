package com.tm.common.hashing;


import com.tm.common.encryption.Enums;

/**
 *   Title: HashServiceImpl
 * FileName: HashServiceImpl.java
 *   @version 1.0
 *   @Created date: 28-11-2017
 *   @author Next Sphere Technologies
 *   Copyright: Copyright © Next Sphere Technologies 2017.
 */
public class HashServiceImpl implements HashService {
    private HashProvider hashProvider;

    public HashServiceImpl(Enums.HashingAlgorithm algorithm) {
        switch (algorithm) {
            case SHA256:
                hashProvider = new SHA256();
                break;
            default:
                hashProvider = new SHA512();
        }
    }

    @Override
    public String generateHash(String text, String salt) throws Exception {
        return hashProvider.generateHash(text, salt);
    }
}
