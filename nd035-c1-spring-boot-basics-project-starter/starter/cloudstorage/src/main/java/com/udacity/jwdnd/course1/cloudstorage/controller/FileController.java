package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/home")  // Base path for this controller
public class FileController {

    private final FileServiceManager fileServiceManager;
    private final UserServiceManager userServiceManager;
    private final NoteServiceManager noteServiceManager;
    private final CredentialServiceManager credentialServiceManager;
    private final EncryptionService encryptionService;

    @Autowired
    public FileController(FileServiceManager fileServiceManager, UserServiceManager userServiceManager,NoteServiceManager noteServiceManager,CredentialServiceManager credentialServiceManager,EncryptionService encryptionService) {
        this.fileServiceManager = fileServiceManager;
        this.userServiceManager = userServiceManager;
        this.noteServiceManager = noteServiceManager;
        this.credentialServiceManager = credentialServiceManager;
        this.encryptionService = encryptionService;


    }
    /**
     * Handles GET requests for the home page.
     * Retrieves and adds user-specific data to the model for rendering.
     *
     * @param authentication the authentication object to get the current user's details
     * @param newFile the form for file-related operations (not used in this method but included for potential form binding)
     * @param newNote the form for note-related operations (not used in this method but included for potential form binding)
     * @param newCredential the form for credential-related operations (not used in this method but included for potential form binding)
     * @param model the model to pass data to the view
     * @return the name of the view to be rendered, which is "home"
     */

    @GetMapping
    public String getHomePage(
            Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model) {
        Integer userId = getUserId(authentication);
        model.addAttribute("files", this.fileServiceManager.getFileListings(userId));
        model.addAttribute("notes", noteServiceManager.getNoteListings(userId));
        model.addAttribute("credentials", credentialServiceManager.getCredentialListings(userId));
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }


    @PostMapping
    public String uploadFile(
    Authentication authentication,
    @ModelAttribute("newFile") FileForm fileForm,
    @ModelAttribute("newNote") NoteForm noteForm,
    @ModelAttribute("newCredential") CredentialForm credentialForm,
    Model model) throws IOException {
        //Get username for authentication
        String userName = authentication.getName();
        //Get user details from UserService Management
        User user = userServiceManager.getUser(userName);
        if (user == null) {
            model.addAttribute("result", "error");
            model.addAttribute("message", "User not found.");
            return "result";
        }
        Integer userId = user.getUserId();
        String[] existingFiles = fileServiceManager.getFileListings(userId);
        //Get uploadedFile
        MultipartFile uploadedFile= fileForm.getFile();
        if (uploadedFile == null || uploadedFile.isEmpty()) {
            model.addAttribute("result", "error");
            model.addAttribute("message", "Please select a file to upload.");
            return "result";
        }
        String uploadedFileName = uploadedFile.getOriginalFilename();
        if (uploadedFileName == null) {
            model.addAttribute("result", "error");
            model.addAttribute("message", "File name is missing.");
            return "result";
        }
// Here a check is done if duplicate files were uploaded
        //Create list that contains existing files and checks if uploaded file is present in the list
        List<String> fileList = Arrays.asList(existingFiles);
        boolean isDuplicate = fileList.contains(uploadedFileName);
        if (isDuplicate) {
            model.addAttribute("result", "error");
            model.addAttribute("message", "You have tried to add a duplicate file.");
        } else {
            fileServiceManager.uploadFile(uploadedFile, userName);
            model.addAttribute("result", "success");
        }

        // Update file listings in the model
        model.addAttribute("files", fileServiceManager.getFileListings(userId));

        return "result";
    }

        @GetMapping(
                value = "/get-file/{fileName}",
                produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
        )

        public @ResponseBody
        byte[] getFile(@PathVariable String fileName) {
            return fileServiceManager.getFile(fileName).getFileData();
        }

    @GetMapping(value = "/delete-file/{fileName}")
    public String deleteFile(
            Authentication authentication, @PathVariable String fileName,
            @ModelAttribute("newFile") FileForm fileForm,
            @ModelAttribute("newNote") NoteForm NoteForm,
            @ModelAttribute("newCredential") CredentialForm credentialForm,
            Model model) {
        fileServiceManager.deleteFile(fileName);
        // Get username from authtencation and get userId
        Integer userId = getUserId(authentication);
        model.addAttribute("files", fileServiceManager.getFileListings(userId));
        model.addAttribute("result", "success");
        return "result";
    }
    private Integer getUserId(Authentication authentication) {
        // Retrieves the user id based on the authenticated user
        String userName = authentication.getName();
        User user = userServiceManager.getUser(userName);
        return user.getUserId();
    }
}
