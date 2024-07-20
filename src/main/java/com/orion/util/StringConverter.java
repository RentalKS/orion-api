package com.orion.util;

public class StringConverter {

    public static String spacesToCamelCase(String input) {
        StringBuilder camelCase = new StringBuilder();
        boolean capitalizeNext = false;

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (currentChar == ' ') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                camelCase.append(Character.toUpperCase(currentChar));
                capitalizeNext = false;
            } else {
                camelCase.append(Character.toLowerCase(currentChar));
            }
        }

        return camelCase.toString();
    }


}
