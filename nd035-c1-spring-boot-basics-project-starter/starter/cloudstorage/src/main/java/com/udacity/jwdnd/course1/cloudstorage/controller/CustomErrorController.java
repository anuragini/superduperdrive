package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Custom error controller to handle errors and display a custom error page.
 */
@Controller
public class CustomErrorController implements ErrorController {
    /**
     * Handles error and returns an error view
     *
     * @return the name of the error view template
     */
    @RequestMapping("/error")
    public String handleError() {
        return "error"; // Return an error view
    }
    /**
     * Returns the error path.
     *
     * @return an empty string as error path
     * defined by Request Mapping annotation
     */
    @Override
    public String getErrorPath() {
        return "";
    }
}
