package pl.coderstrust.generators;

import pl.coderstrust.model.Company;

public class CompanyGenerator {
  public static Company getRandomCompany(){
    Long id = IdGenerator.getNextId();
    String name = NameGenerator.getNextName();
    return new Company(id, name, null, null, null, null, null);
  }
}

