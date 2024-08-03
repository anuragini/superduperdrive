package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileServiceManager {

    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    @Autowired
    public FileServiceManager(FileMapper fileMapper,UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }
    public String[] getFileListings(Integer userId) {
        return fileMapper.getFileListings(userId);
    }

 public void uploadFile(MultipartFile multipartFile, String userName) throws IOException {
     byte[] fileData = readFileData(multipartFile);
     //Get file data
     String fileName = multipartFile.getOriginalFilename();
     String contentType = multipartFile.getContentType();
     String fileSize = String.valueOf(multipartFile.getSize());

     Integer userId = getUserId(userName);

     File file = new File(0, fileName, contentType, fileSize, userId, fileData);
     fileMapper.insert(file);
 }
 //Reads file Data from a Multipart file
    //fileData is a byte array
    //IO Expection thrown if IO error occurs
    private byte[] readFileData(MultipartFile multipartFile) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream();
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            byte[] data = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(data)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            return buffer.toByteArray();
        }
    }
    //Gets userId based on the userName
    private Integer getUserId(String userName) {
        return userMapper.getUser(userName).getUserId();
    }

    //Responsible for retriving the file based on file name
    public File getFile(String fileName){
        return fileMapper.getFile(fileName);
    }
    //deleteFile will delete file also based on the fileName
    //fileName is the name of the file to be deleted
    public void deleteFile(String fileName) {
        fileMapper.deleteFile(fileName);
    }
}






