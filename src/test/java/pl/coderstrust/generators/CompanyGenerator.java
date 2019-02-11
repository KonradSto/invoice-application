package pl.coderstrust.generators;

import pl.coderstrust.model.Company;

public class CompanyGenerator {

  private static Long id = IdGenerator.getNextId();
  private static String name = CompanyNameGenerator.getNextName();

  public static Company getRandomCompany() {
    return new Company(id, name, null, null, null, null, null);
  }
}
