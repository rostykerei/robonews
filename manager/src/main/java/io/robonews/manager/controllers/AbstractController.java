/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.manager.controllers;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class AbstractController {

    protected final static String INFO_MESSAGE = "infoMessage";
    protected final static String ERROR_MESSAGE = "errorMessage";
    protected final static String EXCEPTION = "exception";

    protected void addModelInfoMessage(Model model, String message) {
        model.addAttribute(INFO_MESSAGE, message);
    }

    protected void addModelErrorMessage(Model model, Exception exception, String message) {
        model.addAttribute(ERROR_MESSAGE, message);
        model.addAttribute(EXCEPTION, exception);
    }

    protected void addModelErrorMessage(Model model, String message) {
        model.addAttribute(ERROR_MESSAGE, message);
    }

    protected void addRedirectInfoMessage(RedirectAttributes model, String message) {
        model.addFlashAttribute(INFO_MESSAGE, message);
    }

    protected void addRedirectErrorMessage(RedirectAttributes model, Exception exception, String message) {
        model.addFlashAttribute(ERROR_MESSAGE, message);
        model.addFlashAttribute(EXCEPTION, exception);
    }

    protected void addRedirectErrorMessage(RedirectAttributes model, String message) {
        model.addFlashAttribute(ERROR_MESSAGE, message);
    }
}
