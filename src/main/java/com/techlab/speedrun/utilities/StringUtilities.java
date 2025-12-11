package com.techlab.speedrun.utilities;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class StringUtilities {

    public boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public String capitalizeWords(String text) {
        if (isEmpty(text)) {
            return text;
        }
        
        String[] words = text.trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase())
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }

    public String sanitizeInput(String input) {
        if (isEmpty(input)) {
            return input;
        }
        return input.trim().replaceAll("\\s+", " ");
    }

    public boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    }

    public boolean containsOnlyLettersAndSpaces(String text) {
        if (isEmpty(text)) {
            return false;
        }
        return text.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    public static boolean isValidLength(String text, int min, int max) {
        if (text == null) {
            return false;
        }
        int length = text.trim().length();
        return length >= min && length <= max;
    }
}