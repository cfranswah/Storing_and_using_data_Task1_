import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class InventoryManager {
    private final Inventory inventory;
    private final SaleHistory saleHistory;

    public InventoryManager() {
        inventory = new Inventory();
        saleHistory = new SaleHistory();
    }

    public void processCommands(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processCommand(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processCommand(String line) {
        String[] parts = line.split(" ");

        if (parts.length > 0) {
            String command = parts[0];
            switch (command) {
                case "ADD":
                    String itemName = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);
                    Item item = new Item(itemName, quantity, price);
                    inventory.addItem(item);
                    break;
                case "SELL":
                    String sellItemName = parts[1];
                    int sellQuantity = Integer.parseInt(parts[2]);
                    double sellPrice = Double.parseDouble(parts[3]);
                    sellItem(sellItemName, sellQuantity, sellPrice);
                    break;
                case "RETURN":
                    String returnItemName = parts[1];
                    int returnQuantity = Integer.parseInt(parts[2]);
                    double returnPrice = Double.parseDouble(parts[3]);
                    returnItem(returnItemName, returnQuantity, returnPrice);
                    break;
                case "DONATE":
                    String donateItemName = parts[1];
                    int donateQuantity = Integer.parseInt(parts[2]);
                    donateItem(donateItemName, donateQuantity);
                    break;
                case "WRITEOFF":
                    String writeOffItemName = parts[1];
                    int writeOffQuantity = Integer.parseInt(parts[2]);
                    writeOffItem(writeOffItemName, writeOffQuantity);
                    break;
                case "CHECK":
                    checkInventory();
                    break;
                case "PROFIT":
                    calculateProfit();
                    break;
                default:
                    // Invalid command, handle appropriately
                    break;
            }
        }
    }

    private void sellItem(String itemName, int quantity, double price) {
        if (inventory.hasEnoughQuantity(itemName, quantity)) {
            Item soldItem = inventory.removeItem(itemName, quantity);
            saleHistory.recordSale(soldItem, quantity, price);
        } else {
            System.out.println("SELL/DONATE/WRITEOFF more items than available quantity.");
        }
    }

    private void returnItem(String itemName, int quantity, double price) {
        if (saleHistory.hasSaleList(itemName, quantity)) {
            Item returnedItem = saleHistory.cancelSale(itemName, quantity, price);
            inventory.addItem(returnedItem);
        } else {
            System.out.println("RETURN: No suitable sale record found.");
        }
    }

    private void donateItem(String itemName, int quantity) {
        if (inventory.hasEnoughQuantity(itemName, quantity)) {
            inventory.removeItem(itemName, quantity);
        } else {
            System.out.println("SELL/DONATE/WRITEOFF more items than available quantity.");
        }
    }

    private void writeOffItem(String itemName, int quantity) {
        if (inventory.hasEnoughQuantity(itemName, quantity)) {
            inventory.removeItem(itemName, quantity);
        } else {
            System.out.println("SELL/DONATE/WRITEOFF more items than available quantity.");
        }
    }

    private void checkInventory() {
        System.out.println("Current Inventory:");
        for (Item item : inventory.getAllItems()) {
            System.out.println(item.getName() + ": " + item.getQuantity());
        }
    }

    private void calculateProfit() {
        double totalProfit = 0.0;

        for (SaleList saleRecord : saleHistory.getSales()) {
            Item item = saleRecord.getItem();
            int quantity = saleRecord.getQuantity();
            double salePrice = saleRecord.getPrice();
            double purchasePrice = item.getPrice();

            totalProfit += (salePrice - purchasePrice) * quantity;
        }

        System.out.println("Profit/Loss: $" + String.format("%.2f", totalProfit));
    }

    public static void main(String[] args) {
        List<String> inputFiles = new ArrayList<>();
        inputFiles.add("/Users/cherylfranswah/Desktop/production/task1dsa/input1.txt");
        inputFiles.add("/Users/cherylfranswah/Desktop/production/task1dsa/input2.txt");
        // Add more input file paths as needed

        InventoryManager inventoryManager = new InventoryManager();
        for (String filePath : inputFiles) {
            System.out.println("Processing file: " + filePath);
            inventoryManager.processCommands(filePath);
        }
    }
}
