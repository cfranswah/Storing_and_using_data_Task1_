import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<String, Item> items;

    public Inventory() {
        items = new HashMap<>();
    }

    public void addItem(Item item) {
        items.put(item.getName(), item);
    }

    public Item removeItem(String itemName, int quantity) {
        Item item = items.get(itemName);
        if (item != null) {
            int currentQuantity = item.getQuantity();
            if (currentQuantity >= quantity) {
                if (currentQuantity == quantity) {
                    items.remove(itemName);
                } else {
                    item.setQuantity(currentQuantity - quantity);
                }
                return new Item(item.getName(), quantity, item.getPrice());
            }
        }
        return null; // Item not found or insufficient quantity
    }

    public boolean hasEnoughQuantity(String itemName, int quantity) {
        Item item = items.get(itemName);
        return item != null && item.getQuantity() >= quantity;
    }

    public Item[] getAllItems() {
        return items.values().toArray(new Item[0]);
    }
}
