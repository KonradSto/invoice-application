import java.math.BigDecimal;

public enum Vat {
  _0(BigDecimal.valueOf(0)),
  _5(BigDecimal.valueOf(0.05)),
  _8(BigDecimal.valueOf(0.08)),
  _23(BigDecimal.valueOf(0.23));

  private final BigDecimal rate;

  Vat(BigDecimal rate) {
    this.rate = rate;
  }

  BigDecimal getNumericValue() {
    return rate;
  }
}
