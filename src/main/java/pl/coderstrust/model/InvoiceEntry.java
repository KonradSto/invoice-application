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
@Entity
public class InvoiceEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    private final String productName;
    private final double quantity;
    private final String unit;
    private final BigDecimal price;
    private final BigDecimal netValue;
    private final BigDecimal grossValue;
    private final Vat vatRate;

    @JsonCreator
    public InvoiceEntry(@JsonProperty("id") Long id,
        @JsonProperty("productName") String productName,
        @JsonProperty("quantity") double quantity,
        @JsonProperty("unit") String unit,
        @JsonProperty("price") BigDecimal price,
        @JsonProperty("netValue") BigDecimal netValue,
        @JsonProperty("grossValue") BigDecimal grossValue,
        @JsonProperty("vatRate") Vat vatRate) {

        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.netValue = netValue;
        this.grossValue = grossValue;
        this.vatRate = vatRate;
    }

    private InvoiceEntry() {
        this.id = null;
        this.productName = null;
        this.quantity = 0;
        this.unit = null;
        this.price = null;
        this.netValue = null;
        this.grossValue = null;
        this.vatRate = null;
    }

    @ApiModelProperty(value = "The id of invoice entry.", dataType = "Long", position = -1)
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

    @ApiModelProperty(value = "product net price per unit", example = "19.99")
    public BigDecimal getPrice() {
        return price;
    }

    @ApiModelProperty(value = "total net value = (net price per unit)*quantity ", example = "29.99")
    public BigDecimal getNetValue() {
        return netValue;
    }

    @ApiModelProperty(value = "total gross value (total net value after appropriate VAT rate applied)", example = "34.99")
    public BigDecimal getGrossValue() {
        return grossValue;
    }

    @ApiModelProperty(value = "VAT rate to be applied to net value ie. 0.23 means 23% VAT", example = "VAT_23")
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
            && netValue.equals(invoiceEntry.netValue)
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
            + ", netValue=" + netValue
            + ", grossValue=" + grossValue
            + ", vatRate=" + vatRate
            + '}';
    }
}
