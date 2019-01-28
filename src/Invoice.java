import java.time.LocalDate;
import java.util.List;

public class Invoice {

  private Long id;
  private String number;
  private LocalDate issuedDate;
  private LocalDate dueDate;
  private Company seller;
  private Company buyer;
  private List<InvoiceEntry> entries;

  public Invoice(Long id, String number, LocalDate issuedDate, LocalDate dueDate, Company seller, Company buyer, List<InvoiceEntry> entries) {
    this.id = id;
    this.number = number;
    this.issuedDate = issuedDate;
    this.dueDate = dueDate;
    this.seller = seller;
    this.buyer = buyer;
    this.entries = entries;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public LocalDate getIssuedDate() {
    return issuedDate;
  }

  public void setIssuedDate(LocalDate issuedDate) {
    this.issuedDate = issuedDate;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public Company getSeller() {
    return seller;
  }

  public void setSeller(Company seller) {
    this.seller = seller;
  }

  public Company getBuyer() {
    return buyer;
  }

  public void setBuyer(Company buyer) {
    this.buyer = buyer;
  }

  public List<InvoiceEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<InvoiceEntry> entries) {
    this.entries = entries;
  }
}
