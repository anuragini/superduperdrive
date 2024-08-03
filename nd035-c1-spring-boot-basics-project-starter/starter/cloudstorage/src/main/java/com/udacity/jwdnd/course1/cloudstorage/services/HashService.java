package com.udacity.jwdnd.course1.cloudstorage.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Provides hashing functionalities using PBKDF2 with HmacSHA1 algorithm.
 * This service is used to securely hash passwords before storing them in the database.
 */
@Service
public class HashService {

    private final Logger logger = LoggerFactory.getLogger(HashService.class);

    /**
     * Generates a hashed value for the provided data using a random salt.
     *
     * @param data The data to be hashed (e.g., password)
     * @param salt A random string used to improve security (should be stored with the hashed value)
     * @return The hashed value as a Base64 encoded string
     * @throws RuntimeException If an error occurs during hashing
     */
    public String getHashedValue(String data, String salt) {
        byte[] hashedValue = null;

        KeySpec spec = new PBEKeySpec(data.toCharArray(), salt.getBytes(), 5000, 128); // PBKDF2 with HmacSHA1, 5000 iterations, 128 bit output
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hashedValue = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error generating hash value", e); // Re-throw with cause for better error handling
        }

        return Base64.getEncoder().encodeToString(hashedValue);
    }
}


