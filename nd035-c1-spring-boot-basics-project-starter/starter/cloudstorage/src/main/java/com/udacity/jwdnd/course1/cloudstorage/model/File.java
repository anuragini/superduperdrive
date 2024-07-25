package com.udacity.jwdnd.course1.cloudstorage.model;

public class File {
    private int fileId;
    private static String fileName;
    private int fileSize;
    private static String contentType;
    private int userId;
    private byte [] fileData;

    public File(int fileId, String fileName, String fileSize, String contentType, int userId, byte [] fileData){
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileSize = Integer.parseInt(fileSize);
        File.contentType = contentType;
        this.userId = userId;
        this.fileData = fileData;



    }
    public File(){

    }

    public int getFileId(){
        return fileId;
    }
    public void setFileId(int fileId){
        this.fileId = fileId;

    }

    public String getFileName(){
        return fileName;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;

    }
    public int getFileSize(){
        return fileSize;
    }
    public void setFileSize(int fileSize){
        this.fileSize = fileSize;

    }
    public String getContentType(){
        return contentType;
    }
    public void setContentType(String contentType){
        this.contentType = contentType;

    }
    public int getUserId(){
        return userId;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }

    public byte [] getFileData() {
        return fileData;
    }
    public void setFileData(byte [] fileData){
        this.fileData = fileData;

    }
    public void setFilename(String originalFilename) {
    }

    public String getFilename() {
        return fileName;
    }

    public void setFileSize(String s) {
    }
}
