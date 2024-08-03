package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;
//Service is responisble for managing credentials
@Service
public class CredentialServiceManager {
    private final UserMapper userMapper;
    private final CredentialMapper credentialMapper;
//CredentialServiceManager constructor
    //user mapper interacts with the user data
    //credential mapper interacts with the credentail data
    public CredentialServiceManager(UserMapper userMapper, CredentialMapper credentialMapper) {
        this.userMapper = userMapper;
        this.credentialMapper = credentialMapper;
    }
    /**
*Adding a new credential to the database
    *@param url of the credential
    *userName of the user
    *UserName of the credential
    *encryption key
    *encrypted password
     */

   // public void addCredential(String url, String userName, String credentialUserName, String key, String password) {
        //Integer userId = userMapper.getUser(userName).getUserId();
        //Credential credential = new Credential(0, url, credentialUserName, key, password, userId);
        //credentialMapper.insert(credential);
   // }
    public void addCredential(String url, String userName, String credentialUserName, String key, String password) {
        User user = userMapper.getUser(userName);
        if (user == null) {
            throw new NullPointerException("User not found for userName: " + userName);
        }

        Integer userId = user.getUserId();
        if (userId == null) {
            throw new NullPointerException("User ID is null for userName: " + userName);
        }

        if (credentialMapper == null) {
            throw new NullPointerException("CredentialMapper is not initialized");
        }

        Credential credential = new Credential(0, url, credentialUserName, key, password, userId);
        credentialMapper.insert(credential);
    }

    /**
*Will retrive list of credentials for specific user
 * @param userId id of the user
 * return an array of credential objects
 */
    public Credential[] getCredentialListings(Integer userId) {
        return credentialMapper.getCredentialListings(userId);
    }

    /**
     * Retrives spefic credential by its ID
     * @param noteId
     * @return credential object
     */
    public Credential getCredential(Integer noteId){return credentialMapper.getCredential(noteId);}


    /**
     * Deletes credential
     * @param noteId
     * Needs the id of the credential to delete
     */
    public void deleteCredential(Integer noteId) {
        credentialMapper.deleteCredential(noteId);
    }
    /**
     * Updates exisiting credential in the database
     * @param credentialid id of the credential to update
     * @param newUserName new username for the credential
     * @param url new url for the credential
     * @param key new encryption key
     * @param password new password
     * Needs the id of the credential to delete
     */
    public void updateCredential(Integer credentialid, String newUserName, String url, String key, String password) {
        credentialMapper.updateCredential(credentialid, newUserName, url, key, password);
    }
}
