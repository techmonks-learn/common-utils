package com.tm.common.hashing;

/**
 *   Title: HashProvider
 * FileName: HashProvider.java
 *   @version 1.0
 *   @Created date: 28-11-2017
 *   @author Next Sphere Technologies
 *   Copyright: Copyright © Next Sphere Technologies 2017.
 */
public interface HashProvider {
    String generateHash(String text, String salt) throws Exception;
}
