package pl.coderstrust.database;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pl.coderstrust.model.Invoice;

public class InMemoryDatabase implements Database{

  private Map<Long, Invoice> invoiceMap = new HashMap<>();

  private static Long nextId = 1L;

  @Override
  public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
    if (invoice == null){
      throw new DatabaseOperationException("Invoice cannot be null");
    }
    if (invoice.getId() == null){
      invoiceMap.put(nextId++, invoice); // do osobnej metody priv "insert". w środku pola final = getXX

    }else{
      //metoda "update" z 2 warunkami - id znajduje się w bazie i nie.
    }
    return invoice;
  }
  //2 metody do kopiowania pol invoice. jedna z parametrami invoice, druga z parametrem new long id.

  @Override
  public void deleteInvoice(Long id) {
    invoiceMap.remove(id);
  }

  @Override
  public Invoice getInvoice(Long id) throws DatabaseOperationException {
    if (!invoiceMap.containsKey(id)){
      throw new DatabaseOperationException("Invoice of id: " + id + " does not exist");
    }
    // sprawdzić czy invoice istnieje - jeśli tak - return invoiceMap.get(id)

    return invoiceMap.get(id);
  }

  @Override
  public Collection<Invoice> getAllInvoices() {
    return invoiceMap.values();
  }

  @Override
  public void deleteAllInvoices() {
    invoiceMap.clear();
  }

  @Override
  public boolean invoiceExists(Long id) {
    return invoiceMap.containsKey(id);
  }

  @Override
  public long countInvoices() {
    return invoiceMap.size();
  }
}
