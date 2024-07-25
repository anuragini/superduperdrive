package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.Principal;


@Controller
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;
    File file;
    @GetMapping
    public String home(Model model) {
        model.addAttribute("fileForm", new FileForm());  // Add FileForm to the model
        return "home";
    }
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, Principal principal, Model model){


        //String userName = principal.getName();
        //User user = userService.getUserByUsername(userName);

        if (file.isEmpty()) {
            model.addAttribute("errorMessage", "File is empty!");
            return "redirect:/home";
        }
        try{

        File newFile = new File();
       newFile.setFilename(file.getOriginalFilename());
        newFile.setContentType(file.getContentType());
        newFile.setFileSize(String.valueOf(file.getSize()));
        newFile.setUserId(123);
        newFile.setFileData(file.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error uploading file!");
            return "redirect:/home";
        }

        return "redirect:/";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id) throws IOException {
        File file;
        file = fileService.getFileById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body((Resource) new ByteArrayResource(file.getFileData()));
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable Integer fileId) {
        fileService.deleteFile(String.valueOf(fileId));
        return "redirect:/home";
    }
}
