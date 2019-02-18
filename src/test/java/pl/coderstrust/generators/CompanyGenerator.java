package pl.coderstrust.generators;

import pl.coderstrust.model.Company;

public class CompanyGenerator {

    private Long id = CompanyIdGenerator.getNextId();
    private String name = new CompanyNameGenerator().getNextName();

    public Company getRandomCompany() {
        return new Company(id, name, null, null, null, null, null);
    }
}
