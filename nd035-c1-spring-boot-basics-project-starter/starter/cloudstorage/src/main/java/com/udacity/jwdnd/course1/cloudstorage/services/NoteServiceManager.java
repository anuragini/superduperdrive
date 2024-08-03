package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;
/**
 * Manages note data access and manipulation functionalities.
 * This service interacts with the Note and User mappers
 * to perform CRUD operations on notes within the application.
 */
@Service
public class NoteServiceManager {
    private final UserMapper userMapper;
    private final NoteMapper noteMapper;

    public NoteServiceManager(UserMapper userMapper, NoteMapper noteMapper) {
        this.userMapper = userMapper;
        this.noteMapper = noteMapper;
    }

    public Note[] getNoteListings(Integer userId) {
        // Retrieves a list of notes associated with the specified user id
        return noteMapper.getNotesForUser(userId);
    }
    public Note getNote(Integer noteId) {
        // Retrieves a specific note based on its id
        return noteMapper.getNote(noteId);
    }

    public void addNote(String title, String description, String userName) {
        // Creates a new note and associates it with the specified user
        Integer userId = userMapper.getUser(userName).getUserId();
        Note note = new Note( title, description, userId);
        noteMapper.insert(note);
    }

    public void updateNote(Integer noteId, String title, String description) {
        // Updates an existing note with the provided details
        noteMapper.updateNote(noteId, title, description);
    }


    public void deleteNote(Integer noteId) {
        // Deletes a note based on its id
        noteMapper.deleteNote(noteId);
    }



}
