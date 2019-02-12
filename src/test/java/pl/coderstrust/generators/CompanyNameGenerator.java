package pl.coderstrust.generators;

import java.util.Random;

public class CompanyNameGenerator {

  private final String BIG_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private final String SMALL_LETTERS = "abcdefghijklmnopqrstuvwxyz";
  private final int randomNameLength = 10;
  private String randomName = "";
  private char[] text = new char[randomNameLength];
  private Random random = new Random();

  public String getNextName() {
    text[0] = BIG_LETTERS.charAt(random.nextInt(BIG_LETTERS.length()));
    for (int i = 1; i < randomNameLength; i++) {
      text[i] = SMALL_LETTERS.charAt(random.nextInt(SMALL_LETTERS.length()));
    }
    for (int i = 0; i < text.length; i++) {
      randomName += text[i];
    }
    return randomName;
  }
}
