package io.robonews.console.datatable;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DatatableParams {

    int maxLength() default 100;

    String[] sortFields() default {};

}
