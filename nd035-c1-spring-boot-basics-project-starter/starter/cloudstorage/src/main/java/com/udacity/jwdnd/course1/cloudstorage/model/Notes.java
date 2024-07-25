package com.udacity.jwdnd.course1.cloudstorage.model;

public class Notes {
    private int noteId;
    private String title;
    private String description;
    private int userId;

    public Notes(int noteId, String title, String description, int userId) {
        this.noteId = noteId;
        this.description = description;
        this.userId = userId;
    }

    public Notes(String title, String description) {
    }

    public Integer getNoteId() {
        return 0;
    }

    public String getTitle() {
        return "";
    }

    public String getDescription() {
        return "";
    }
}
