import java.util.LinkedList;

public class SaleHistory {
    private LinkedList<SaleList> sales;

    public SaleHistory() {
        sales = new LinkedList<>();
    }

    public void recordSale(Item item, int quantity, double price) {
        SaleList saleList = new SaleList(item, quantity, price);
        sales.addFirst(saleList); // Adding at the beginning for easy cancellation
    }

    public boolean hasSaleList(String itemName, int quantity) {
        for (SaleList record : sales) {
            if (record.getItem().getName().equals(itemName) && record.getQuantity() >= quantity) {
                return true;
            }
        }
        return false;
    }

    public Item cancelSale(String itemName, int quantity, double price) {
        for (SaleList record : sales) {
            if (record.getItem().getName().equals(itemName) && record.getQuantity() >= quantity) {
                sales.remove(record);
                return new Item(itemName, quantity, price);
            }
        }
        return null; // No suitable sale record found
    }

    public LinkedList<SaleList> getSales() {
        return sales;
    }
}

