package pl.coderstrust;

public class Company {

  private Long id;
  private String name;
  private String address;
  private String taxId;
  private String accountNumber;
  private String phoneNumber;
  private String email;

  public Company(Long id,
      String name,
      String address,
      String taxId,
      String accountNumber,
      String phoneNumber,
      String email) {
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

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getTaxId() {
    return taxId;
  }

  public void setTaxId(String taxId) {
    this.taxId = taxId;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
