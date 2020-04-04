package com.tm.common.encryption;

public class EncryptionServiceImpl implements EncryptionService {
    private EncryptionProvider hashProvider;

    public EncryptionServiceImpl(Enums.EncryptionAlgorithm algorithm) {
        switch (algorithm) {
            case AES_GCM:
                hashProvider = new AES();
                break;
            case AES_ECB:
                hashProvider = new AESECB();
                break;
            default:
                hashProvider = new AES();
        }
    }

    @Override
    public String encryptText(String plainText, String secretKey, Boolean staticKey) throws Exception {
        return hashProvider.encryptText(plainText, secretKey, staticKey);
    }

    @Override
    public String decryptText(String encryptedText, String secretKey) throws Exception {
        return hashProvider.decryptText(encryptedText, secretKey);
    }

    @Override
    public String createSaltKey(int size) {
        return hashProvider.createSaltKey(size);
    }

    @Override
    public String getSecretKey() throws Exception {
        return hashProvider.getSecretKey();
    }
}
