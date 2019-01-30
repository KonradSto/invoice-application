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
}
