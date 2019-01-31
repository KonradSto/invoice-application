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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InvoiceEntry that = (InvoiceEntry) o;
    return Double.compare(that.quantity, quantity) == 0 &&
        id.equals(that.id) &&
        productName.equals(that.productName) &&
        unit.equals(that.unit) &&
        price.equals(that.price) &&
        nettValue.equals(that.nettValue) &&
        grossValue.equals(that.grossValue) &&
        vatRate == that.vatRate;
  }
}
