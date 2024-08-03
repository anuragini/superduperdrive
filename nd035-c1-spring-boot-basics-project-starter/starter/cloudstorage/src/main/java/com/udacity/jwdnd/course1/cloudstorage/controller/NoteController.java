package com.udacity.jwdnd.course1.cloudstorage.controller;
import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteServiceManager;
import com.udacity.jwdnd.course1.cloudstorage.services.UserServiceManager;
import com.udacity.jwdnd.course1.cloudstorage.services.UserServiceManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//Controller for Notes
@Controller
@RequestMapping("note")
public class NoteController {
    private final NoteServiceManager noteServiceManager;
    private final UserServiceManager userServiceManager;

    public NoteController(NoteServiceManager noteServiceManager, UserServiceManager userServiceManager) {
        this.noteServiceManager = noteServiceManager;
        this.userServiceManager = userServiceManager;
    }
    //Handles requests for the home page
    @GetMapping
    public String getHomePage(
            Authentication authentication,
            @ModelAttribute("newFile") FileForm fileForm,
            @ModelAttribute("newNote") NoteForm noteForm,
            @ModelAttribute("newCredential") CredentialForm credentialForm,
            Model model) {
        // Get userId from authentication
        Integer userId = getUserId(authentication);
        model.addAttribute("notes", this.noteServiceManager.getNoteListings(userId));

        return "home";
    }


//Responsible for handling the additon of new notes or updating exisiting notes
    @PostMapping("/add-note")
    public String newNote(
            Authentication authentication,
            @ModelAttribute("newFile") FileForm fileForm,
            @ModelAttribute("newNote") NoteForm noteForm,
            @ModelAttribute("newCredential") CredentialForm credentialForm,
            Model model) {
        String userName = authentication.getName();
        String title = noteForm.getTitle();
        String noteIdStr = noteForm.getNoteId();
        String description = noteForm.getDescription();
        if (isNoteIdEmpty(noteIdStr)) {
            addNewNote(title,description,userName);
        } else {
            Note existingNote = getNote(Integer.parseInt(noteIdStr));
            noteServiceManager.updateNote(existingNote.getNoteId(), title, description);
        }
        Integer userId = getUserId(authentication);
        model.addAttribute("notes", noteServiceManager.getNoteListings(userId));
        model.addAttribute("result", "success");

        return "result";
    }
    //Checking if noteId is empty
    //NoteIdStr is noteId as a string
    //return true if noteId is empty false if not
    private boolean isNoteIdEmpty(String noteIdStr) {
        return noteIdStr == null || noteIdStr.trim().isEmpty();
    }
    private void addNewNote(String title, String description, String userName){
        noteServiceManager.addNote(title, description, userName);
    }
    private void updateExisitingNote(String noteIdStr,String title,String description){
        int noteId = Integer.parseInt(noteIdStr);
        Note existingNote = getNote(noteId);
        if (existingNote != null) {
            noteServiceManager.updateNote(noteId, title, description);
        } else {
            // Handle case where the note does not exist
            throw new IllegalArgumentException("Note with ID " + noteId + " doesn't exist.");
        }
    }


//Handles retriving a note by its Id
@GetMapping("/get-note/{noteId}")
public @ResponseBody Note getNote(@PathVariable Integer noteId) {
    return noteServiceManager.getNote(noteId);
}

@GetMapping(value = "/delete-note/{noteId}")
    public String deleteNote(
            Authentication authentication,
            @PathVariable Integer noteId,Model model){
    //Responsible for deleting the note
        noteServiceManager.deleteNote(noteId);
        //Getting userId from authentication
        Integer userId = getUserId(authentication);

        model.addAttribute("notes", noteServiceManager.getNoteListings(userId));
        model.addAttribute("result", "success");

        return "result";
    }
    //Helper to get  userId for authentication purposes
    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userServiceManager.getUser(userName);
        return user.getUserId();
    }
}






