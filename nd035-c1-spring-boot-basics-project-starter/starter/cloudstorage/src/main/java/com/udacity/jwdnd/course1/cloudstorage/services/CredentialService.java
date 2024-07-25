package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CredentialService {
    public void deleteCredential(Integer id) {
    }

    public void saveOrUpdate(Credential credential) {
    }

    public List<Credential> getCredentialsByUserId(Integer userId) {
        return null;
    }

    public Object getCredentialListings(Integer userId) {
        return null;
    }

    public void addCredential(String newUrl, String userName, String encodedKey, String encryptedPassword, String password) {
        UserMapper userMapper = null;
        Integer userId = userMapper.getUser(userName).getUserId();
        Credential credential = new Credential(newUrl, userName, encodedKey, encryptedPassword);
        CredentialMapper credentialMapper = null;
        credentialMapper.insert(credential);


    }

    public void updateCredential(Integer credentialid, String userName, String newUrl, String encodedKey, String encryptedPassword) {
    }
}