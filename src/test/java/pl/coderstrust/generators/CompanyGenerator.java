package pl.coderstrust.generators;

import pl.coderstrust.model.Company;

public class CompanyGenerator {

    private static Long id = IdGenerator.getNextId();
    private static String name =  WordGenerator.getRandomWord();
    private static String address = WordGenerator.getRandomWord();
    private static String taxId = NumberGenerator.getRandomNumber();
    private static String accountId = NumberGenerator.getRandomNumber();
    private static String phoneNumber = NumberGenerator.getRandomNumber();

    public static Company getRandomCompany() {
        return new Company(id, name, address, taxId, accountId, phoneNumber, name + "@xyz.com");
    }
}
