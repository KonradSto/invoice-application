package pl.coderstrust.generators;

import java.util.Random;

public class CompanyNameGenerator {

  private static final String BIG_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String SMALL_LETTERS = "abcdefghijklmnopqrstuvwxyz";
  private static final int randomNameLength = 10;
  private static String randomName = "";
  private static char[] text = new char[randomNameLength];
  private static Random random = new Random();

  public static  String getNextName() {
    text[0] = BIG_LETTERS.charAt(random.nextInt(BIG_LETTERS.length()));
    for (int i =1; i < randomNameLength; i++){
      text[i] = SMALL_LETTERS.charAt(random.nextInt(SMALL_LETTERS.length()));
    }
    for (int i = 0; i < text.length; i++){
      randomName += text[i];
    }
    return randomName;
  }
}
