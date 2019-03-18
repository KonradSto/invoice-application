package pl.coderstrust.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private final Long id;
    private final String name;
    private final String address;
    private final String taxId;
    private final String accountNumber;
    private final String phoneNumber;
    private final String email;

    @JsonCreator
    public Company(@JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("address") String address,
        @JsonProperty("taxId") String taxId,
        @JsonProperty("accountNumber") String accountNumber,
        @JsonProperty("phoneNumber") String phoneNumber,
        @JsonProperty("email") String email) {

        this.id = id;
        this.name = name;
        this.address = address;
        this.taxId = taxId;
        this.accountNumber = accountNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTaxId() {
        return taxId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Company company = (Company) obj;
        return id.equals(company.id)
            && name.equals(company.name)
            && address.equals(company.address)
            && taxId.equals(company.taxId)
            && accountNumber.equals(company.accountNumber)
            && phoneNumber.equals(company.phoneNumber)
            && email.equals(company.email);
    }

    @Override
    public String toString() {
        return "Company{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", address='" + address + '\''
            + ", taxId='" + taxId + '\''
            + ", accountNumber='" + accountNumber + '\''
            + ", phoneNumber='" + phoneNumber + '\''
            + ", email='" + email + '\''
            + '}';
    }
}
