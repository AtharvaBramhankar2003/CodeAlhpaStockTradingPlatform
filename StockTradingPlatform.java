import java.util.*;
import java.io.*;

class Stock {
    String symbol;
    String name;
    double price;

    Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}

class User {
    String username;
    double balance;
    HashMap<String, Integer> portfolio;

    User(String username, double balance) {
        this.username = username;
        this.balance = balance;
        this.portfolio = new HashMap<>();
    }

    void displayPortfolio(HashMap<String, Stock> stockMarket) {
        System.out.println("\n\u001B[36m--- Your Portfolio ---\u001B[0m");
        double totalValue = 0.0;
        for (String symbol : portfolio.keySet()) {
            int quantity = portfolio.get(symbol);
            Stock s = stockMarket.get(symbol);
            double value = quantity * s.price;
            totalValue += value;
            System.out.printf("\u001B[33m%s (%s): %d shares @ %.2f = %.2f\u001B[0m\n",
                    s.name, symbol, quantity, s.price, value);
        }
        System.out.printf("\u001B[35mTotal Portfolio Value: ₹%.2f\u001B[0m\n", totalValue);
        System.out.printf("\u001B[32mCash Balance: ₹%.2f\u001B[0m\n", balance);
    }
}

public class StockTradingPlatform {

    static HashMap<String, Stock> stockMarket = new HashMap<>();
    static User user;

    public static void main(String[] args) {
        initializeMarket();
        Scanner sc = new Scanner(System.in);

        System.out.print("\u001B[34mEnter your name: \u001B[0m");
        String name = sc.nextLine();
        user = new User(name, 10000);

        int choice;
        do {
            displayMenu();
            choice = sc.nextInt();
            switch (choice) {
                case 1 -> displayMarket();
                case 2 -> buyStock(sc);
                case 3 -> sellStock(sc);
                case 4 -> user.displayPortfolio(stockMarket);
                case 5 -> savePortfolio();
                case 6 -> System.out.println("\u001B[31mExiting... Goodbye!\u001B[0m");
                default -> System.out.println("\u001B[31mInvalid choice!\u001B[0m");
            }
        } while (choice != 6);
    }

    static void initializeMarket() {
        stockMarket.put("AAPL", new Stock("AAPL", "Apple", 150));
        stockMarket.put("INFY", new Stock("INFY", "Infosys", 1350));
        stockMarket.put("GOOG", new Stock("GOOG", "Google", 2800));
        stockMarket.put("TSLA", new Stock("TSLA", "Tesla", 800));
    }

    static void displayMenu() {
        System.out.println("\n\u001B[36m========== Stock Trading Platform ==========");
        System.out.println("1. View Market");
        System.out.println("2. Buy Stock");
        System.out.println("3. Sell Stock");
        System.out.println("4. View Portfolio");
        System.out.println("5. Save Portfolio to File");
        System.out.println("6. Exit");
        System.out.print("Enter choice: \u001B[0m");
    }

    static void displayMarket() {
        System.out.println("\n\u001B[34m--- Available Stocks ---\u001B[0m");
        for (Stock s : stockMarket.values()) {
            System.out.printf("\u001B[33m%s (%s) - ₹%.2f\u001B[0m\n", s.name, s.symbol, s.price);
        }
    }

    static void buyStock(Scanner sc) {
        System.out.print("Enter stock symbol to buy: ");
        String symbol = sc.next().toUpperCase();
        if (!stockMarket.containsKey(symbol)) {
            System.out.println("\u001B[31mStock not found.\u001B[0m");
            return;
        }
        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();
        Stock s = stockMarket.get(symbol);
        double cost = s.price * qty;
        if (user.balance < cost) {
            System.out.println("\u001B[31mInsufficient funds.\u001B[0m");
            return;
        }
        user.balance -= cost;
        user.portfolio.put(symbol, user.portfolio.getOrDefault(symbol, 0) + qty);
        System.out.println("\u001B[32mStock purchased successfully.\u001B[0m");
    }

    static void sellStock(Scanner sc) {
        System.out.print("Enter stock symbol to sell: ");
        String symbol = sc.next().toUpperCase();
        if (!user.portfolio.containsKey(symbol)) {
            System.out.println("\u001B[31mYou don't own this stock.\u001B[0m");
            return;
        }
        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();
        int ownedQty = user.portfolio.get(symbol);
        if (qty > ownedQty) {
            System.out.println("\u001B[31mNot enough shares to sell.\u001B[0m");
            return;
        }
        Stock s = stockMarket.get(symbol);
        double earnings = s.price * qty;
        user.balance += earnings;
        if (qty == ownedQty) user.portfolio.remove(symbol);
        else user.portfolio.put(symbol, ownedQty - qty);
        System.out.println("\u001B[32mStock sold successfully.\u001B[0m");
    }

    static void savePortfolio() {
        try (PrintWriter out = new PrintWriter("portfolio.txt")) {
            out.println("Username: " + user.username);
            out.println("Balance: " + user.balance);
            out.println("Portfolio:");
            for (Map.Entry<String, Integer> entry : user.portfolio.entrySet()) {
                out.printf("%s %d\n", entry.getKey(), entry.getValue());
            }
            System.out.println("\u001B[32mPortfolio saved to portfolio.txt\u001B[0m");
        } catch (IOException e) {
            System.out.println("\u001B[31mError saving portfolio.\u001B[0m");
        }
    }
}
