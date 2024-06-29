package com.hanghae.lecture.common.validation;

import java.util.Objects;
import java.util.function.Predicate;

public class CommonValidation {

    public static boolean isNullCheck(Object obj) {
        Predicate<Object> predicate = o -> Objects.isNull(o);
        return predicate.test(obj);
    }
}
