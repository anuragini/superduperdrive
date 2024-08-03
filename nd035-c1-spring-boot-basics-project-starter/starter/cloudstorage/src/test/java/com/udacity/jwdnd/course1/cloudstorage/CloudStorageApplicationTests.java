package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    protected WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver");
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        assertEquals("Login", driver.getTitle());
    }

    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }

    private void doLogIn(String userName, String password) {
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-button")));
        WebElement loginButton = driver.findElement(By.id("submit-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));
    }

    @Test
    public void testRedirection() {
        doMockSignUp("Redirection", "Test", "RT", "123");
        assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    @Test
    public void testBadUrl() {
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }

    @Test
    public void testLargeUpload() {
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
    }

    @Test
    public void nonDuplicateFile() {
        // Sign up a new user
        doMockSignUp("NonDuplicate", "Test", "NDT", "123");

        // Log in as that user
        doLogIn("NDT", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "/Users/anuraginisinha/Documents/udacity/nd035-c1-spring-boot-basics-project-starter/starter/cloudstorage/Screenshot 2024-06-02 at 7.44.16 PM.png"; // Use a small test file for simplicity

        // Upload a file that is not a duplicate
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();

        try {
            // Verify that the file upload is successful
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
            Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("File upload failed");
            Assertions.fail("File upload did not succeed within the timeout period");
        }

        // Check that there's no error message about duplicate files
        Assertions.assertFalse(driver.getPageSource().contains("You have tried to add a duplicate file. Click here to continue."));
    }

    @Test
    public void LoginSignUpVerify() {
        doMockSignUp("Testing", "Test", "Tester", "123");
        // Log in as that user
        doLogIn("Tester", "123");
        //verify accessible home page
        //  WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
        //logout
        WebElement logoutButton = driver.findElement(By.id("logoutButton"));
        logoutButton.click();
        // Check that the URL /home is not accessible anymor
        driver.get("http://localhost:" + this.port + "/home");
        assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

    }

    @Test
    public void UnauthorizedAccess() {
        driver.get("http://localhost:" + this.port + "/home");
        assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
        driver.get("http://localhost:" + this.port + "/signup");
        assertEquals("http://localhost:" + this.port + "/signup", driver.getCurrentUrl());
    }

    @Test
    public void NoteCreationAndDisplay() {
        // Sign up a new user
        doMockSignUp("Note", "Test", "NT", "123");

        // Log in as that user
        doLogIn("NT", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Click the Notes tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        // Click Add New Note button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn-info.float-right")));
        WebElement addNewNoteButton = driver.findElement(By.cssSelector(".btn-info.float-right"));
        addNewNoteButton.click();

        // Write title and description
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.click();
        noteTitle.sendKeys("Test Note Title");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.click();
        noteDescription.sendKeys("Test Note Description");

        // Click Save Changes
        WebElement saveChangesButton = driver.findElement(By.id("btnSaveChanges"));
        saveChangesButton.click();

        // Verify that success message is shown
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));

        // Click the "here" button to continue
        WebElement hereButton = driver.findElement(By.id("here"));
        hereButton.click();
        // Go to Notes tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        // Check that title and description match what is shown
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tableNoteTitle")));
        WebElement displayedNoteTitle = driver.findElement(By.id("tableNoteTitle"));
        WebElement displayedNoteDescription = driver.findElement(By.id("tableNoteDescription"));

        assertEquals("Test Note Title", displayedNoteTitle.getText());
        assertEquals("Test Note Description", displayedNoteDescription.getText());
    }

    @Test
    public void editNote() {
        // Sign up a new user
        doMockSignUp("EditNote", "Test", "EN", "123");

        // Log in as that user
        doLogIn("EN", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Click the Notes tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        // Click Add New Note button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn-info.float-right")));
        WebElement addNewNoteButton = driver.findElement(By.cssSelector(".btn-info.float-right"));
        addNewNoteButton.click();

        // Write initial title and description
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.click();
        noteTitle.sendKeys("Initial Note Title");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.click();
        noteDescription.sendKeys("Initial Note Description");

        // Click Save Changes
        WebElement saveChangesButton = driver.findElement(By.id("btnSaveChanges"));
        saveChangesButton.click();

        // Verify that success message is shown
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));

        // Click the "here" button to continue
        WebElement hereButton = driver.findElement(By.id("here"));
        hereButton.click();
        // Go to Notes tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        // Check that title and description match what is shown
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tableNoteTitle")));
        WebElement displayedNoteTitle = driver.findElement(By.id("tableNoteTitle"));
        WebElement displayedNoteDescription = driver.findElement(By.id("tableNoteDescription"));

        assertEquals("Initial Note Title", displayedNoteTitle.getText());
        assertEquals("Initial Note Description", displayedNoteDescription.getText());

        // Click the Edit button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn-success")));
        WebElement editButton = driver.findElement(By.cssSelector(".btn-success"));
        editButton.click();

        // Change the title and description
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.clear();
        noteTitle.sendKeys("Edited Note Title");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.clear();
        noteDescription.sendKeys("Edited Note Description");

        // Click Save Changes button
        saveChangesButton = driver.findElement(By.id("btnSaveChanges"));
        saveChangesButton.click();

        // Verify that success message is shown
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));

        // Click the "here" button to continue
        hereButton = driver.findElement(By.id("here"));
        hereButton.click();
        // Go to Notes tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        // Verify the changes are shown
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tableNoteTitle")));
        displayedNoteTitle = driver.findElement(By.id("tableNoteTitle"));
        displayedNoteDescription = driver.findElement(By.id("tableNoteDescription"));

        assertEquals("Edited Note Title", displayedNoteTitle.getText());
        assertEquals("Edited Note Description", displayedNoteDescription.getText());
    }

    @Test
    public void deleteNote() {
        // Sign up a new user
        doMockSignUp("DeleteNote", "Test", "DN", "123");

        // Log in as that user
        doLogIn("DN", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Click the Notes tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        // Click Add New Note button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn-info.float-right")));
        WebElement addNewNoteButton = driver.findElement(By.cssSelector(".btn-info.float-right"));
        addNewNoteButton.click();

        // Write title and description
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.click();
        noteTitle.sendKeys("Note To Delete");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.click();
        noteDescription.sendKeys("This note will be deleted");

        // Click Save Changes
        WebElement saveChangesButton = driver.findElement(By.id("btnSaveChanges"));
        saveChangesButton.click();

        // Verify that success message is shown
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));

        // Click the "here" button to continue
        WebElement hereButton = driver.findElement(By.id("here"));
        hereButton.click();
        // Go to Notes tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        // Check that title and description match what is shown
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tableNoteTitle")));
        WebElement displayedNoteTitle = driver.findElement(By.id("tableNoteTitle"));
        WebElement displayedNoteDescription = driver.findElement(By.id("tableNoteDescription"));

        assertEquals("Note To Delete", displayedNoteTitle.getText());
        assertEquals("This note will be deleted", displayedNoteDescription.getText());

        // Click the Delete button (similar to edit button logic)
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn-danger")));
        WebElement deleteButton = driver.findElement(By.cssSelector(".btn-danger"));
        deleteButton.click();

        // Verify that success message is shown
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));

        // Click the "here" button to continue
        hereButton = driver.findElement(By.id("here"));
        hereButton.click();
        // Go to Notes tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        // Verify the deleted note is no longer there
        List<WebElement> noteTitles = driver.findElements(By.id("tableNoteTitle"));
        for (WebElement title : noteTitles) {
            assertNotEquals("Note To Delete", title.getText());
        }
    }

    //sign up a new user
    //log in with new account
    //Navigate to the credentials tab
    //click the add new credential button
    //enter URL
    //enter username
    //enter password
    //click the save changes button
    //verify success dialog shows and click here button
    //go to credentials tab
    //verify new credential is showing
    //verify credential url is showing
    //verify credential username is showing
    //veirfy credential password is showing
    @Test
    public void credentialCreationAndDisplay() {
        // Sign up a new user
        doMockSignUp("Credential", "Test", "CT", "123");

        // Log in as that user
        doLogIn("CT", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        // Navigate to the credentials tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();

        // Click the Add New Credential button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCredentialButton")));
        WebElement addNewCredentialButton = driver.findElement(By.id("addCredentialButton"));
        addNewCredentialButton.click();

        // Enter URL
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement credentialUrl = driver.findElement(By.id("credential-url"));
        credentialUrl.click();
        credentialUrl.sendKeys("http://example.com");

        // Enter username
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement credentialUsername = driver.findElement(By.id("credential-username"));
        credentialUsername.click();
        credentialUsername.sendKeys("exampleUser");

        // Enter password
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement credentialPassword = driver.findElement(By.id("credential-password"));
        credentialPassword.click();
        credentialPassword.sendKeys("examplePassword");

        // Click Save Changes
        WebElement saveChangesButton = driver.findElement(By.id("btnSaveCredential"));
        saveChangesButton.click();

        // Verify that success message is shown
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));

        // Click the "here" button to continue
        WebElement hereButton = driver.findElement(By.id("here"));
        hereButton.click();
        // Go to credentials tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();

        // Verify new credential is showing
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        WebElement displayedCredentialUrl = driver.findElement(By.id("credentialTable"));
        WebElement displayedCredentialUsername = driver.findElement(By.id("tableCredentialUsername"));
        WebElement displayedCredentialPassword = driver.findElement(By.id("tableCredentialPassword"));

    }

    @Test
    public void credentialViewEditView() {
        //signup user
        //login user
        //go to credentials tab
        //create credential
            //click add a new credential button
            // fill in URL, username, and password
            //click save changes button
            //click here to continue button
        //navigate to credentials tab
        //verify password shown is encrypted
        //click edit button
        //verify password shown when editing password field is unencryted
        //edit URL, username, and password
        //verify that t
    }

}




