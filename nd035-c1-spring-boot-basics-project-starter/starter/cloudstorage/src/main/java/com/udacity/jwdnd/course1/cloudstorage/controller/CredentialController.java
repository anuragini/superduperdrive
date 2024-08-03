package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialServiceManager;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserServiceManager;
import com.udacity.jwdnd.course1.cloudstorage.services.UserServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("credential")
public class CredentialController {

    private final CredentialServiceManager credentialServiceManager;
    private final EncryptionService encryptionService;
    private final UserServiceManager userServiceManager;
    //Dependencies required for credential management
    public CredentialController(CredentialServiceManager credentialServiceManager,
                                EncryptionService encryptionService,
                                UserServiceManager userServiceManager) {
        this.credentialServiceManager = credentialServiceManager;
        this.encryptionService = encryptionService;
        this.userServiceManager = userServiceManager;
    }

    // Controller methods here
    @GetMapping
    public String getHomePage(
            Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            @ModelAttribute("newNote") NoteForm newNote, Model model) {
        // Get the authenticated user
        String userName = authentication.getName();
        // Retrieve user's credentials and add to model
        User user = userServiceManager.getUser(userName);
        model.addAttribute("credentials", this.credentialServiceManager.getCredentialListings(user.getUserId()));
        // Add encryption service to model for view access
        model.addAttribute("encryptionService", encryptionService);
//Return homepage view
        return "home";
    }


    @PostMapping("add-credential")
    public String newCredential(
            Authentication authentication,
            // @ModelAttribute("newFile") FileForm newFile,
            // @ModelAttribute("newNote") NoteForm newNote,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model) {

        String userName = authentication.getName();
        String newUrl = newCredential.getUrl();
        String credentialIdStr = newCredential.getCredentialid();
        String password = newCredential.getPassword();

        // Generate a random key for encryption
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);

        // Encrypt the password
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        // Determine whether to add or update credential
        if (credentialIdStr.isEmpty()) {
            credentialServiceManager.addCredential(newUrl, userName, newCredential.getUserName(), encodedKey, encryptedPassword);
        } else {
            Credential existingCredential = getCredential(Integer.parseInt(credentialIdStr));
            credentialServiceManager.updateCredential(existingCredential.getCredentialid(), newCredential.getUserName(), newUrl, encodedKey, encryptedPassword);
        }

        // Refresh credential list and add to model
        User user = userServiceManager.getUser(userName);
        model.addAttribute("credentials", credentialServiceManager.getCredentialListings(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");

        return "result";
    }

    @GetMapping(value = "/get-credential/{credentialid}")
    public Credential getCredential(@PathVariable Integer credentialid) {
        // Retrieves a credential by its id
        return credentialServiceManager.getCredential(credentialid);
    }

    @GetMapping(value = "/delete-credential/{credentialId}")
    public String deleteCredential(
            Authentication authentication,
            @PathVariable Integer credentialid,
            // @ModelAttribute("newCredential") CredentialForm newCredential,
            // @ModelAttribute("newFile") FileForm newFile,
            // @ModelAttribute("newNote") NoteForm newNote,
            Model model) {

        // Delete the credential
        credentialServiceManager.deleteCredential(credentialid);

        // Refresh credential list for the user
        String userName = authentication.getName();
        User user = userServiceManager.getUser(userName);
        model.addAttribute("credentials", credentialServiceManager.getCredentialListings(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");

        return "result";
    }


    //Will retrive authenticated User based on object
     // authentication the authentication object containing user details
     //return the authenticated User object
    private User getAuthenticatedUser(Authentication authentication) {
        String userName = authentication.getName();
        return userServiceManager.getUser(userName);
    }

    /**Adds necessary attributes to the model for home page
    @param model to model is the model to add atributes to
    userId is the id of the authenticated user
     **/
    private void addModelAttributes(Model model, Integer userId) {
        model.addAttribute("credentials", credentialServiceManager.getCredentialListings(userId));
        model.addAttribute("encryptionService", encryptionService);
    }

     /**Generates a base64 encoded key.
     returns a base64 encoded key as a String\
      **/

    private String generateEncodedKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
    private Integer getUserId(Authentication authentication) {
        // Retrieves the user id based on the authenticated user
        String userName = authentication.getName();
        User user = userServiceManager.getUser(userName);
        return user.getUserId();
    }

}









