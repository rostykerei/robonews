/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlValidator implements ConstraintValidator<Url, String> {
    @Override
    public void initialize(Url url) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s == null || s.isEmpty() || io.robonews.service.text.tools.UrlValidator.isValid(s);


    }
}
