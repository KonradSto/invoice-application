package pl.coderstrust.utils;

public class ArgumentValidator {

    public static <T> void ensureNotNull(T argument, String paramName) {
        if (argument == null) {
            throw new IllegalArgumentException(String.format("%s cannot be null", paramName));
        }
    }
}
