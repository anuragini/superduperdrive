package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests for Note Creation, Viewing, Editing, and Deletion.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoteTests {
    /**
     * Test that edits an existing note and verifies that the changes are displayed.
     */
    @Test
    public void testDelete() {
        String noteTitle = "My Note";
        String noteDescription = "This is my note.";
        HomePage homePage = signUpAndLogin();
        createNote(noteTitle, noteDescription, homePage);
        homePage.navToNotesTab();
        WebDriver driver = null;
        homePage = new HomePage(driver);
        Assertions.assertFalse(homePage.noNotes(driver));
        //deleteNote(homePage);
        Assertions.assertTrue(homePage.noNotes(driver));
    }

    private HomePage signUpAndLogin() {
        return null;
    }

    /*
    private void deleteNote(HomePage homePage) {
        homePage.deleteNote();
        ResultPage resultPage = new ResultPage(driver);
        resultPage.wait();
    }

     */

    /**
     * Test that creates a note, and verifies it is displayed.
     */
    @Test
    public void testCreateAndDisplay() {
        String noteTitle = "My Note";
        String noteDescription = "This is my note.";
        HomePage homePage = signUpAndLogin();
        createNote(noteTitle, noteDescription, homePage);
        homePage.navToNotesTab();
        WebDriver driver = null;
        homePage = new HomePage(driver);
        Notes note = homePage.getFirstNote();
        Assertions.assertEquals(noteTitle, note.getTitle());
        Assertions.assertEquals(noteDescription, note.getDescription());
        //deleteNote(homePage);
        homePage.logout();
    }

    /**
     * Test that edits an existing note and verifies that the changes are displayed.
     */
    @Test
    public void testModify() {
        String noteTitle = "My Note";
        String noteDescription = "This is my note.";
        HomePage homePage = signUpAndLogin();
        createNote(noteTitle, noteDescription, homePage);
        homePage.navToNotesTab();
        WebDriver driver = null;
        homePage = new HomePage(driver);
        homePage.editNote();
        String modifiedNoteTitle = "My Modified Note";
        homePage.modifyNoteTitle(modifiedNoteTitle);
        String modifiedNoteDescription = "This is my modified note.";
        homePage.modifyNoteDescription(modifiedNoteDescription);
        homePage.saveNoteChanges();
        //ResultPage resultPage = new ResultPage(driver);
        //resultPage.clickOk();
        homePage.navToNotesTab();
        Notes note = homePage.getFirstNote();
        Assertions.assertEquals(modifiedNoteTitle, note.getTitle());
        Assertions.assertEquals(modifiedNoteDescription, note.getDescription());
    }

    private void createNote(String noteTitle, String noteDescription, HomePage homePage) {
        homePage.navToNotesTab();
        homePage.addNewNote();
        homePage.setNoteTitle(noteTitle);
        homePage.setNoteDescription(noteDescription);
        homePage.saveNoteChanges();
        //ResultPage resultPage = new ResultPage(driver);
        //resultPage.clickOk();
        homePage.navToNotesTab();
    }
}