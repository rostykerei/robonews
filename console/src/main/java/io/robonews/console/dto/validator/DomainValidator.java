/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DomainValidator implements ConstraintValidator<Domain, String> {
    @Override
    public void initialize(Domain domain) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return io.robonews.service.text.tools.DomainValidator.isValid(s);
    }
}
