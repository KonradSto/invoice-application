package pl.coderstrust;

public enum Vat {
  VAT_0(0.0f),
  VAT_5(0.05f),
  VAT_8(0.08f),
  VAT_23(0.23f);

  private final float rate;

  Vat(float rate) {
    this.rate = rate;
  }

  float getNumericValue() {
    return rate;
  }
}
