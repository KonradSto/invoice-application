package pl.coderstrust.generators;

import pl.coderstrust.model.Company;

public class CompanyGenerator {
    public static Company getRandomCompany() {
        Long id = IdGenerator.getNextId();
        String name = WordGenerator.getRandomWord();
        String address = WordGenerator.getRandomWord();
        String taxId = NumberGenerator.getRandomNumber();
        String accountId = NumberGenerator.getRandomNumber();
        String phoneNumber = NumberGenerator.getRandomNumber();
        return new Company(id, name, address, taxId, accountId, phoneNumber, name + "@xyz.com");
    }
}
