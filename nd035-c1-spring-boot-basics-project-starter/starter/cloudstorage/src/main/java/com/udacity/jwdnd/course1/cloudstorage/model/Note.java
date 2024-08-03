package com.udacity.jwdnd.course1.cloudstorage.model;

public class Note {
    private Integer noteId;
    private String title;
    private String description;
    private Integer userId;

    // Constructor, getters, and setters
    public Note(Integer noteId, String title, String description, Integer userId) {
        this.noteId = noteId;
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    public Note(String title, String description, Integer userId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
