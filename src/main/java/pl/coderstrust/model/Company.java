package pl.coderstrust.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(value = "Company", description = "Companyyyyyy")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ApiModelProperty(value = "Company id",example = "123-34-123")
    public Long getId() {
        return id;
    }

    @ApiModelProperty(value = "Invoice issuer",example = "InvoiceSoft Ltd.")
    public String getName() {
        return name;
    }

    @ApiModelProperty(value = "Invoice issuer address",example = "Clock Street, 12-345, Invoicetown, Co. Wexford, Ireland")
    public String getAddress() {
        return address;
    }

    @ApiModelProperty(value = "Tax id",example = "342-456-345")
    public String getTaxId() {
        return taxId;
    }

    @ApiModelProperty(value = "Account number",example = "45-56-5676")
    public String getAccountNumber() {
        return accountNumber;
    }

    @ApiModelProperty(value = "Phone number",example = "(12)345-456-887")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @ApiModelProperty(value = "Email address",example = "jsmith@mail.com")
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
