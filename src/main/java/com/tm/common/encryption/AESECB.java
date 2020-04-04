package com.tm.common.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESECB implements EncryptionProvider {
    /**
     * @param plainText
     * @param secretKey in Base64 encoded
     * @return
     */

    @Override
    public String encryptText(String plainText, String secretKey, Boolean staticKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        SecretKey secKey = getSecretKey(secretKey);
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(byteCipherText);
    }


    @Override
    public String decryptText(String encryptedText, String secretKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        SecretKey secKey = getSecretKey(secretKey);
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        return new String(aesCipher.doFinal(Base64.getDecoder().decode(encryptedText)));
    }


    @Override
    public String createSaltKey(int size) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[size];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * @return base64 encoded string
     * @throws Exception
     */

    @Override
    public String getSecretKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        // The AES key size in number of bits
        generator.init(128);
        SecretKey secKey = generator.generateKey();
        return Base64.getEncoder().encodeToString(secKey.getEncoded());
    }

    /**
     * Converts from string to SecretKey
     *
     * @param encodedKey
     * @return
     */
    private SecretKey getSecretKey(String encodedKey) {
        // decode the base64 encoded string
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        // rebuild key using SecretKeySpec
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}
