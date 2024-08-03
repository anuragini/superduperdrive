package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * This service interacts with the User mapper and HashService
 * to perform operations on user accounts within the application,
 * including user creation, verification, and retrieval.
 */
@Service
public class UserServiceManager {

    private final UserMapper userMapper;
    private final HashService hashService;

    public UserServiceManager(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }

    public int createUser(User user) {
        // Check if the username is available
        if (!isUsernameAvailable(user.getUsername())) {
            throw new IllegalArgumentException("The username already exists.");
        }

        // Generate salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        // Hash password
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);

        // Set salt and hashed password
        user.setSalt(encodedSalt);
        user.setPassword(hashedPassword);

        // Insert user into the database
        return userMapper.insert(user);
    }

    public User getUser(String username) {
        // Retrieves a user based on their username
        return userMapper.getUser(username);
    }

    public User getUser(Integer userId) {
        // Retrieves a user based on their user ID
        return userMapper.getUserId(userId);
    }

}
