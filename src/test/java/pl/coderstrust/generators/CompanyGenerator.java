package pl.coderstrust.generators;

import java.util.Random;

import pl.coderstrust.model.Company;

public class CompanyGenerator {

    private static Random random = new Random();

    public static Company getRandomCompany() {
        Long id = IdGenerator.getNextId();
        String name = WordGenerator.getRandomWord();
        String address = WordGenerator.getRandomWord();
        String taxId = getRandomNumberAsString();
        String accountId = getRandomNumberAsString();
        String phoneNumber = getRandomNumberAsString();
        return new Company(id, name, address, taxId, accountId, phoneNumber, name + "@xyz.com");
    }

    public static Company getRandomCompanyWithSpecificId(Long id) {
        String name = WordGenerator.getRandomWord();
        String address = WordGenerator.getRandomWord();
        String taxId = getRandomNumberAsString();
        String accountId = getRandomNumberAsString();
        String phoneNumber = getRandomNumberAsString();
        return new Company(id, name, address, taxId, accountId, phoneNumber, name + "@xyz.com");
    }

    private static String getRandomNumberAsString() {
        return String.format("%05d%05d", random.nextInt(99999), random.nextInt(99999));
    }
}
