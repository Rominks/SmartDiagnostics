package com.***REMOVED***.smartdiagnostics.Helpers;

import java.util.regex.Pattern;

public class ValidationHelper {
    // Email validation
    public static boolean patternMatches(String email, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
}
