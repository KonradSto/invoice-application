package pl.coderstrust.generators;

import java.util.Random;

public class CompanyNameGenerator {

    private final String bigLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String smallLetters = "abcdefghijklmnopqrstuvwxyz";
    private final int randomNameLength = 10;
    private char[] text = new char[randomNameLength];
    private Random random = new Random();

    public String getNextName() {
        text[0] = bigLetters.charAt(random.nextInt(bigLetters.length()));
        for (int i = 1; i < randomNameLength; i++) {
            text[i] = smallLetters.charAt(random.nextInt(smallLetters.length()));
        }
        StringBuilder randomNameBuilder = new StringBuilder();
        for (int i = 0; i < text.length; i++) {
            randomNameBuilder.append(text[i]);
        }
        return randomNameBuilder.toString();
    }
}
