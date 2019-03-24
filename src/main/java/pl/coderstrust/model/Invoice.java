package pl.coderstrust.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@ApiModel(value = "Invoice", description = "Invoiceeeeeed")
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    private final String number;
    private final LocalDate issuedDate;
    private final LocalDate dueDate;
    @ManyToOne(cascade = CascadeType.ALL)
    private final Company seller;
    @ManyToOne(cascade = CascadeType.ALL)
    private final Company buyer;
    @ManyToMany(cascade = CascadeType.ALL)
    private final List<InvoiceEntry> entries;

    @JsonCreator
    public Invoice(@JsonProperty("id") Long id,
                   @JsonProperty("number") String number,
                   @JsonProperty("issuedDate") LocalDate issuedDate,
                   @JsonProperty("dueDate") LocalDate dueDate,
                   @JsonProperty("seller") Company seller,
                   @JsonProperty("buyer") Company buyer,
                   @JsonProperty("entries") List<InvoiceEntry> entries) {

        this.id = id;
        this.number = number;
        this.issuedDate = issuedDate;
        this.dueDate = dueDate;
        this.seller = seller;
        this.buyer = buyer;
        this.entries = entries != null ? new ArrayList(entries) : new ArrayList();
    }
@ApiModelProperty(value = "invoice id",example = "1")
    public Long getId() {
        return id;
    }

    @ApiModelProperty(value = "invoice number",example = "2019/03/2")
    public String getNumber() {
        return number;
    }
    @ApiModelProperty(value = "Invoice issue date",example = "2019-03-12")
    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    @ApiModelProperty(value = "Invoice due date",example = "2019-04-12")
    public LocalDate getDueDate() {
        return dueDate;
    }

    public Company getSeller() {
        return seller;
    }

    @ApiModelProperty(value = "Invoice receiver",example = "John Smith")
    public Company getBuyer() {
        return buyer;
    }

    public List<InvoiceEntry> getEntries() {
        return entries;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Invoice invoice = (Invoice) obj;
        return id.equals(invoice.id)
            && number.equals(invoice.number)
            && issuedDate.equals(invoice.issuedDate)
            && dueDate.equals(invoice.dueDate)
            && seller.equals(invoice.seller)
            && buyer.equals(invoice.buyer)
            && entries.equals(invoice.entries);
    }

    @Override
    public String toString() {
        return "Invoice{"
            + "id=" + id
            + ", number='" + number + '\''
            + ", issuedDate=" + issuedDate
            + ", dueDate=" + dueDate
            + ", seller=" + seller
            + ", buyer=" + buyer
            + ", entries=" + entries
            + '}';
    }
}
