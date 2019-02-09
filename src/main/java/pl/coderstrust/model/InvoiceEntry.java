package pl.coderstrust.model;

import java.math.BigDecimal;

public class InvoiceEntry {

    private final Long id;
    private final String productName;
    private final double quantity;
    private final String unit;
    private final BigDecimal price;
    private final BigDecimal nettValue;
    private final BigDecimal grossValue;
    private final Vat vatRate;

    public InvoiceEntry(Long id,
        String productName,
        double quantity,
        String unit,
        BigDecimal price,
        BigDecimal nettValue,
        BigDecimal grossValue,
        Vat vatRate) {

        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.nettValue = nettValue;
        this.grossValue = grossValue;
        this.vatRate = vatRate;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getNettValue() {
        return nettValue;
    }

    public BigDecimal getGrossValue() {
        return grossValue;
    }

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
}
