package pl.coderstrust.generators;

import java.util.Random;

public class NumberGenerator {
    private static Random random = new Random();

    public static String getRandomNumber() {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            number.append(random.nextInt(10));
        }
        return number.toString();
    }
}
