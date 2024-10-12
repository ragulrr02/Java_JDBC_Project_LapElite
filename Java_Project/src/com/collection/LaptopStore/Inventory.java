package com.collection.LaptopStore;

import java.sql.*;
import java.util.*;

public class Inventory {
    private static final Scanner scanner = new Scanner(System.in);
    private List<Laptop> laptopList = new ArrayList<>();

    // Add a laptop to the inventory
    public void addLaptop() {
        System.out.print("Enter laptop name: ");
        String name = scanner.nextLine();

        System.out.print("Enter laptop brand: ");
        String brand = scanner.nextLine();

        System.out.print("Enter laptop processor: ");
        String processor = scanner.nextLine();

        int price;
        while (true) {
            try {
                System.out.print("Enter laptop price: ");
                price = Integer.parseInt(scanner.nextLine());
                if (price < 0) {
                    System.out.println("Price cannot be negative. Please enter a valid price.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Please enter a numeric value.");
            }
        }

        int quantity;
        while (true) {
            try {
                System.out.print("Enter laptop quantity: ");
                quantity = Integer.parseInt(scanner.nextLine());
                if (quantity < 0) {
                    System.out.println("Quantity cannot be negative. Please enter a valid quantity.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please enter a numeric value.");
            }
        }

        Laptop newLaptop = new Laptop(name, brand, processor, price, quantity);
        laptopList.add(newLaptop);  // Add laptop to list

        // SQL logic for adding laptop to the database (if applicable)
        String insertSQL = "INSERT INTO laptop (name, brand, processor, price, quantity) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, newLaptop.getName());
            pstmt.setString(2, newLaptop.getBrand());
            pstmt.setString(3, newLaptop.getProcessor());
            pstmt.setInt(4, newLaptop.getPrice());
            pstmt.setInt(5, newLaptop.getQuantity());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        System.out.println("Laptop added successfully with ID: " + generatedId);
                    } else {
                        System.out.println("Laptop added successfully, but failed to retrieve ID.");
                    }
                }
            } else {
                System.out.println("Failed to add laptop.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding laptop: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Sort laptops by a specific attribute
    public void sortLaptops() {
        System.out.println("Sort laptops by:");
        System.out.println("1. Name");
        System.out.println("2. Brand");
        System.out.println("3. Processor");
        System.out.println("4. Price");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        Comparator<Laptop> comparator = null;

        switch (choice) {
            case "1":
                comparator = Comparator.comparing(Laptop::getName);
                break;
            case "2":
                comparator = Comparator.comparing(Laptop::getBrand);
                break;
            case "3":
                comparator = Comparator.comparing(Laptop::getProcessor);
                break;
            case "4":
                comparator = Comparator.comparingInt(Laptop::getPrice);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        Collections.sort(laptopList, comparator);
        System.out.println("Laptops sorted successfully.");
        displayLaptops(); // Show the sorted list
    }

    // Display all laptops in the inventory
    public void displayLaptops() {
        System.out.println("\nAvailable Laptops:");
        for (Laptop laptop : laptopList) {
            System.out.println(laptop);
        }
    }

    // Search for a laptop by different criteria (name, brand, processor)
    public void searchLaptop() {
        System.out.println("Search by:");
        System.out.println("1. Name");
        System.out.println("2. Brand");
        System.out.println("3. Processor");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        String searchField = "";
        switch (choice) {
            case "1":
                searchField = "name";
                break;
            case "2":
                searchField = "brand";
                break;
            case "3":
                searchField = "processor";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.print("Enter the search value: ");
        String searchValue = scanner.nextLine();

        String searchSQL = "SELECT * FROM laptop WHERE " + searchField + " LIKE ?";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement pstmt = con.prepareStatement(searchSQL)) {

            pstmt.setString(1, "%" + searchValue + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("No laptops found.");
                    return;
                }

                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") +
                            ", Name: " + rs.getString("name") +
                            ", Brand: " + rs.getString("brand") +
                            ", Processor: " + rs.getString("processor") +
                            ", Price: " + rs.getInt("price") +
                            ", Quantity: " + rs.getInt("quantity"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching laptops: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update a laptop's details by ID
    public void updateLaptop() {
        System.out.print("Enter laptop ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        Laptop laptop = findLaptopById(id);
        if (laptop == null) {
            System.out.println("Laptop with ID " + id + " not found.");
            return;
        }

        System.out.print("Enter new name (or press Enter to keep '" + laptop.getName() + "'): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            laptop.setName(newName);
        }

        System.out.print("Enter new brand (or press Enter to keep '" + laptop.getBrand() + "'): ");
        String newBrand = scanner.nextLine();
        if (!newBrand.isEmpty()) {
            laptop.setBrand(newBrand);
        }

        System.out.print("Enter new processor (or press Enter to keep '" + laptop.getProcessor() + "'): ");
        String newProcessor = scanner.nextLine();
        if (!newProcessor.isEmpty()) {
            laptop.setProcessor(newProcessor);
        }

        System.out.print("Enter new price (or press Enter to keep '" + laptop.getPrice() + "'): ");
        String newPriceStr = scanner.nextLine();
        if (!newPriceStr.isEmpty()) {
            try {
                int newPrice = Integer.parseInt(newPriceStr);
                laptop.setPrice(newPrice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Keeping the current price.");
            }
        }

        System.out.print("Enter new quantity (or press Enter to keep '" + laptop.getQuantity() + "'): ");
        String newQuantityStr = scanner.nextLine();
        if (!newQuantityStr.isEmpty()) {
            try {
                int newQuantity = Integer.parseInt(newQuantityStr);
                laptop.setQuantity(newQuantity);
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Keeping the current quantity.");
            }
        }

        // SQL logic for updating the laptop
        String updateSQL = "UPDATE laptop SET name = ?, brand = ?, processor = ?, price = ?, quantity = ? WHERE id = ?";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement pstmt = con.prepareStatement(updateSQL)) {

            pstmt.setString(1, laptop.getName());
            pstmt.setString(2, laptop.getBrand());
            pstmt.setString(3, laptop.getProcessor());
            pstmt.setInt(4, laptop.getPrice());
            pstmt.setInt(5, laptop.getQuantity());
            pstmt.setInt(6, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Laptop updated successfully.");
            } else {
                System.out.println("Failed to update laptop.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating laptop: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Delete a laptop by ID
    public void deleteLaptop() {
        System.out.print("Enter laptop ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        Laptop laptop = findLaptopById(id);
        if (laptop == null) {
            System.out.println("Laptop with ID " + id + " not found.");
            return;
        }

        String deleteSQL = "DELETE FROM laptop WHERE id = ?";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement pstmt = con.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Laptop deleted successfully.");
            } else {
                System.out.println("Failed to delete laptop.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting laptop: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Buy a laptop
    public void buyLaptop() {
        System.out.print("Enter laptop ID to buy: ");
        int id = Integer.parseInt(scanner.nextLine());

        Laptop laptop = findLaptopById(id);
        if (laptop == null) {
            System.out.println("Laptop with ID " + id + " not found.");
            return;
        }

        System.out.println("Laptop details:");
        System.out.println("ID: " + laptop.getId());
        System.out.println("Name: " + laptop.getName());
        System.out.println("Brand: " + laptop.getBrand());
        System.out.println("Processor: " + laptop.getProcessor());
        System.out.println("Price: " + laptop.getPrice());
        System.out.println("Quantity available: " + laptop.getQuantity());

        System.out.print("Enter quantity to buy: ");
        int quantityToBuy = Integer.parseInt(scanner.nextLine());

        if (quantityToBuy > laptop.getQuantity()) {
            System.out.println("Not enough stock available. Available quantity: " + laptop.getQuantity());
            return;
        }

        String updateSQL = "UPDATE laptop SET quantity = ? WHERE id = ?";
        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement pstmt = con.prepareStatement(updateSQL)) {

            pstmt.setInt(1, laptop.getQuantity() - quantityToBuy);
            pstmt.setInt(2, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Laptop bought successfully.");
            } else {
                System.out.println("Failed to update laptop quantity.");
            }
        } catch (SQLException e) {
            System.out.println("Error buying laptop: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Find a laptop by its ID
    public Laptop findLaptopById(int id) {
        Laptop laptop = null;
        String searchSQL = "SELECT * FROM laptop WHERE id = ?";
        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement pstmt = con.prepareStatement(searchSQL)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    laptop = new Laptop(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("brand"),
                            rs.getString("processor"),
                            rs.getInt("price"),
                            rs.getInt("quantity")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding laptop by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return laptop;
    }
}
