package pl.coderstrust.generators;

import pl.coderstrust.model.Company;

public class CompanyGenerator {

    private Long id = IdGenerator.getNextId();
    private String name =  WordGenerator.getRandomWord();
    private String address = WordGenerator.getRandomWord();
    private String taxId = NumberGenerator.getRandomNumber();
    private String accountId = NumberGenerator.getRandomNumber();
    private String phoneNumber = NumberGenerator.getRandomNumber();

    public Company getRandomCompany() {
        return new Company(id, name, address, taxId, accountId, phoneNumber, name + "@xyz.com");
    }
}
