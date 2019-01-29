package pl.coderstrust;

import java.math.BigDecimal;

public class InvoiceEntry {

  private Integer id;
  private String description;
  private BigDecimal value;
  private BigDecimal vatValue;
  private Vat vatRate;

  public InvoiceEntry(Integer id, String description, BigDecimal value, BigDecimal vatValue,
      Vat vatRate) {
    this.id = id;
    this.description = description;
    this.value = value;
    this.vatValue = vatValue;
    this.vatRate = vatRate;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getVatValue() {
    return vatValue;
  }

  public void setVatValue(BigDecimal vatValue) {
    this.vatValue = vatValue;
  }

  public Vat getVatRate() {
    return vatRate;
  }

  public void setVatRate(Vat vatRate) {
    this.vatRate = vatRate;
  }
}
