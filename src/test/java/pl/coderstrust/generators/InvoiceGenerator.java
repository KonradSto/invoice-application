package pl.coderstrust.generators;

import java.time.LocalDate;
import java.util.List;

import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

public class InvoiceGenerator {

  private static Long id = null;
  private static String number = InvoiceNumberGenerator.getNextInvoiceNumber();
  private static LocalDate issueDate = LocalDate.now();
  private static LocalDate dueDate = LocalDate.now();
  private static Company seller = CompanyGenerator.getRandomCompany();
  private static Company buyer = CompanyGenerator.getRandomCompany();
  private static List<InvoiceEntry> entry = null;

  public static Invoice getInvoice(){
    return new Invoice(id, number, issueDate, dueDate, seller, buyer, entry);
  }
}
