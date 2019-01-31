package pl.coderstrust;

import java.math.BigDecimal;

public class InvoiceEntry {

  private Long id;
  private String productName;
  private double quantity;
  private String unit;
  private BigDecimal price;
  private BigDecimal nettValue;
  private BigDecimal grossValue;
  private Vat vatRate;

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

  public void setId(Long id) {
    this.id = id;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public double getQuantity() {
    return quantity;
  }

  public void setQuantity(double quantity) {
    this.quantity = quantity;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getNettValue() {
    return nettValue;
  }

  public void setNettValue(BigDecimal nettValue) {
    this.nettValue = nettValue;
  }

  public BigDecimal getGrossValue() {
    return grossValue;
  }

  public void setGrossValue(BigDecimal grossValue) {
    this.grossValue = grossValue;
  }

  public Vat getVatRate() {
    return vatRate;
  }

  public void setVatRate(Vat vatRate) {
    this.vatRate = vatRate;
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
