package pl.coderstrust.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@ApiModel(value = "Invoice entry")
@ApiModel(value = "Invoiceee entry", description = "Invoice entryyyyy")
@Entity
public class InvoiceEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    private final String productName;
    private final double quantity;
    private final String unit;
    private final BigDecimal price;
    private final BigDecimal nettValue;
    private final BigDecimal grossValue;
    private final Vat vatRate;

    @JsonCreator
    public InvoiceEntry(@JsonProperty("id") Long id,
                        @JsonProperty("productName") String productName,
                        @JsonProperty("quantity") double quantity,
                        @JsonProperty("unit") String unit,
                        @JsonProperty("price") BigDecimal price,
                        @JsonProperty("nettValue") BigDecimal nettValue,
                        @JsonProperty("grossValue") BigDecimal grossValue,
                        @JsonProperty("vatRate") Vat vatRate) {

        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.nettValue = nettValue;
        this.grossValue = grossValue;
        this.vatRate = vatRate;
    }

    @ApiModelProperty(value = "invoice entry id", example = "1")
    public Long getId() {
        return id;
    }

    @ApiModelProperty(value = "product name", example = "soap")
    public String getProductName() {
        return productName;
    }

    @ApiModelProperty(value = "product quantity", example = "2")
    public double getQuantity() {
        return quantity;
    }

    @ApiModelProperty(value = "unit of quantity ie. kg, pc", example = "1")
    public String getUnit() {
        return unit;
    }

    @ApiModelProperty(value = "product nett price per unit", example = "19.99")
    public BigDecimal getPrice() {
        return price;
    }

    @ApiModelProperty(value = "total nett value = (nett price per unit)*quantity ", example = "29.99")
    public BigDecimal getNettValue() {
        return nettValue;
    }

    @ApiModelProperty(value = "total gross value (total nett value after appropriate VAT rate applied)", example = "34.99")
    public BigDecimal getGrossValue() {
        return grossValue;
    }

    @ApiModelProperty(value = "VAT rate to be applied to nett value ie. 0.23 means 23% VAT", example = "0.23")
    public Vat getVatRate() {
        return vatRate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        InvoiceEntry invoiceEntry = (InvoiceEntry) obj;
        return Double.compare(invoiceEntry.quantity, quantity) == 0
            && id.equals(invoiceEntry.id)
            && productName.equals(invoiceEntry.productName)
            && unit.equals(invoiceEntry.unit)
            && price.equals(invoiceEntry.price)
            && nettValue.equals(invoiceEntry.nettValue)
            && grossValue.equals(invoiceEntry.grossValue)
            && vatRate == invoiceEntry.vatRate;
    }

    @Override
    public String toString() {
        return "InvoiceEntry{"
            + "id=" + id
            + ", productName='" + productName + '\''
            + ", quantity=" + quantity
            + ", unit='" + unit + '\''
            + ", price=" + price
            + ", nettValue=" + nettValue
            + ", grossValue=" + grossValue
            + ", vatRate=" + vatRate
            + '}';
    }
}
