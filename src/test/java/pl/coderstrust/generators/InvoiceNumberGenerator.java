package pl.coderstrust.generators;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceNumberGenerator {
  private static String currentYear = String.valueOf(LocalDate.now().getYear());
  private static AtomicLong atomicLong = new AtomicLong(0);

  public static String getNextInvoiceNumber() {
    return atomicLong.incrementAndGet() + "/" + currentYear;
  }
}
