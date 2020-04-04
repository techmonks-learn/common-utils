package com.tm.common.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class AES implements EncryptionProvider {
    private static final int SALT_LENGTH = 64;
    private static final int IV_LENGTH = 12;
    private static final int TAG_BYTE_LENGTH = 16;

    private SecretKey getSecretKey(String base64EncodedKey, byte[] saltBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec ks = new PBEKeySpec(base64EncodedKey.toCharArray(), saltBytes, 2145, 256);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        SecretKey pbeKey = skf.generateSecret(ks);
        return pbeKey;
    }

    /**
     * This encrypts the text message using AES 256 GCM algorithm
     *
     * @param plainText
     * @param encryptionKey
     * @return
     * @throws Exception
     */
    @Override
    public String encryptText(String plainText, String encryptionKey, Boolean staticKey) throws Exception {
        // Create the encrypion SALT, IV and AUTH_TAG
        byte[] salt ;
        byte[] iv ;
        byte[] tag ;
        if (Objects.nonNull(staticKey) && staticKey) {
            salt = new byte[]{-11, -50, 68, -16, 41, 57, 73, -96, -70, -117, 11, 58, 36, -114, -51, 15, -125, 10, 4, 102, -71, 98, 94, -40, -36, 88, 74, -22, -113, -37, 20, 79, -112, 41, 75, -69, -67, -119, -21, 84, 28, 42, -87, -54, -85, -45, 32, -4, 98, 51, 98, -7, 50, -45, -117, -114, -78, -44, 101, 9, 7, -34, 113, -90};
            iv = new byte[]{-31, 127, 45, 52, -70, -124, 99, 12, -36, 1, 30, 51};
            tag = new byte[]{81, 67, 28, -8, 0, -83, -52, -35, 82, 75, 5, -115, -101, 124, 89, 50};
        } else {
            salt = new byte[SALT_LENGTH];
            iv = new byte[IV_LENGTH];
            tag = new byte[TAG_BYTE_LENGTH];

            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);
            random.nextBytes(iv);
            random.nextBytes(tag);
        }
        //Construct the Encryption Key in required format
        SecretKey pbeKey = getSecretKey(encryptionKey, salt);
        GCMParameterSpec ivSpec = new GCMParameterSpec(TAG_BYTE_LENGTH * Byte.SIZE, iv);
        SecretKeySpec newKey = new SecretKeySpec(pbeKey.getEncoded(), "AES");

        //Get the AES/GCM/NoPadding instance and initialize
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

        //Convert the plain text to UTF-8 bytes and do the encryption
        byte[] textBytes = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] updateByte = cipher.update(textBytes);
        byte[] finalByte = cipher.doFinal();

        //Construct the encrypted text
        ByteBuffer encryptedBuffer = ByteBuffer.allocate(updateByte.length + finalByte.length);
        encryptedBuffer.put(updateByte).put(finalByte);

        byte[] encryptedByteArray = encryptedBuffer.array();

        //Construct the final encrypted text in the required format
        //In Java the required format is SALT + IV + ENCRYPTED_TEXT + AUTH_TAG
        //In AES/GCM/NoPadding mode the Auth Tag will be appended at the end of the encrypted text automatically
        ByteBuffer resultBuffer = ByteBuffer.allocate(salt.length + iv.length + encryptedByteArray.length);
        resultBuffer.put(salt).put(iv).put(encryptedByteArray);

        //Encode the result in Base64 string
        return Base64.getEncoder().encodeToString(resultBuffer.array());

    }

    /**
     * Decrypts the encrypted text using AES 256 GCM algorithm
     *
     * @param encryptedText
     * @param encryptionKey
     * @return
     * @throws Exception
     */
    @Override
    public String decryptText(String encryptedText, String encryptionKey) throws Exception {

        //Decode the text from Base64 string to bytes
        byte[] textBytes = Base64.getDecoder().decode(encryptedText);

        // Divide the text bytes into SALT + IV + ENCRYPTED_TEXT + AUTH_TAG
        byte[] salt = Arrays.copyOfRange(textBytes, 0, 64); // 64 bytes
        byte[] iv = Arrays.copyOfRange(textBytes, 64, 76); // 12 bytes
        int index = textBytes.length - 16;
        byte[] tag = Arrays.copyOfRange(textBytes, index, textBytes.length); // 16 bytes
        byte[] text = Arrays.copyOfRange(textBytes, 76, index); // remaining bytes


        //Construct the Encryption Key in required format
        SecretKey pbeKey = getSecretKey(encryptionKey, salt);
        GCMParameterSpec ivSpec = new GCMParameterSpec(TAG_BYTE_LENGTH * Byte.SIZE, iv);
        SecretKeySpec newKey = new SecretKeySpec(pbeKey.getEncoded(), "AES");

        //Get the AES/GCM/NoPadding instance and initialize
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);

        cipher.update(text);
        byte[] decryptedBytes = cipher.doFinal(tag);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


    /**
     * generate salt key
     *
     * @param size
     * @return
     */
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
        generator.init(TAG_BYTE_LENGTH * Byte.SIZE);
        SecretKey secKey = generator.generateKey();
        return Base64.getEncoder().encodeToString(secKey.getEncoded());
    }
}