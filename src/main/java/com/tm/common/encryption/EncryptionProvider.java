package com.tm.common.encryption;

public interface EncryptionProvider {
    String encryptText(String plainText, String secretKey, Boolean staticKey) throws Exception;

    String decryptText(String encryptedText, String secretKey) throws Exception;

    String createSaltKey(int size);

    String getSecretKey() throws Exception;
}