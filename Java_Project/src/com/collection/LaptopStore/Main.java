package com.collection.LaptopStore;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Inventory inventory = new Inventory();

    // ANSI escape codes for colored output
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        boolean loggedIn = false;
        boolean isAdmin = false;

        while (true) {
            if (!loggedIn) {
                showWelcomeMessage();
                System.out.print("Enter username: ");
                String username = scanner.nextLine().trim();
                System.out.print("Enter password: ");
                String password = scanner.nextLine().trim();

                isAdmin = Login.isAdmin(username, password);
                loggedIn = true;

                if (isAdmin) {
                    System.out.println(ANSI_GREEN + "Admin access granted." + ANSI_RESET);
                } else {
                    System.out.println(ANSI_GREEN + "User access granted." + ANSI_RESET);
                }
            }

            if (loggedIn) {
                if (isAdmin) {
                    loggedIn = adminMenu();  
                } else {
                    loggedIn = userMenu();  
                }
            }
        }
    }

    private static void showWelcomeMessage() {
        System.out.println(ANSI_BLUE + "========================================" + ANSI_RESET);
        System.out.println(ANSI_RED + "    Welcome to Ragul Laptop Store" + ANSI_RESET); // Changed to red
        System.out.println(ANSI_RED + "       Management System" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "========================================" + ANSI_RESET);
        System.out.println("Please log in to continue.");
    }

    private static boolean adminMenu() {
        while (true) {
            try {
                System.out.println(ANSI_YELLOW + "\n=== Admin Menu ===" + ANSI_RESET);
                displayAdminMenuOptions();

                String choice = getUserChoice(1, 8);
                switch (choice) {
                    case "1":
                        inventory.addLaptop();
                        break;
                    case "2":
                        inventory.updateLaptop();
                        break;
                    case "3":
                        inventory.deleteLaptop();
                        break;
                    case "4":
                        inventory.displayLaptops();
                        break;
                    case "5":
                        inventory.searchLaptop();
                        break;
                    case "6":
                        System.out.println(ANSI_GREEN + "Logging out..." + ANSI_RESET);
                        return false;  
                    case "7":
                        System.out.println(ANSI_RED + "Exiting the application. Goodbye!" + ANSI_RESET);
                        scanner.close();
                        System.exit(0);
                    case "8":
                        inventory.sortLaptops();
                        break;
                }
            } catch (Exception e) {
                System.out.println(ANSI_RED + "An unexpected error occurred: " + e.getMessage() + ANSI_RESET);
                e.printStackTrace();  
            }
        }
    }

    private static void displayAdminMenuOptions() {
        System.out.println("1. Add Laptop");
        System.out.println("2. Update Laptop");
        System.out.println("3. Delete Laptop");
        System.out.println("4. Show Laptops");
        System.out.println("5. Search Laptop");
        System.out.println("6. Logout");
        System.out.println("7. Exit");
        System.out.println("8. Sort Laptops");
        System.out.print("Choose an option (1-8): ");
    }

    private static String getUserChoice(int min, int max) {
        while (true) {
            String choice = scanner.nextLine().trim();
            if (isNumeric(choice)) {
                int option = Integer.parseInt(choice);
                if (option >= min && option <= max) {
                    return choice;
                }
            }
            System.out.println(ANSI_RED + "Invalid choice. Please enter a number between " + min + " and " + max + "." + ANSI_RESET);
        }
    }

    private static boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    private static boolean userMenu() {
        while (true) {
            try {
                System.out.println(ANSI_YELLOW + "\n=== User Menu ===" + ANSI_RESET);
                displayUserMenuOptions();

                String choice = getUserChoice(1, 6);
                switch (choice) {
                    case "1":
                        inventory.displayLaptops();
                        break;
                    case "2":
                        inventory.searchLaptop();
                        break;
                    case "3":
                        inventory.buyLaptop();
                        break;
                    case "4":
                        inventory.sortLaptops();
                        break;
                    case "5":
                        System.out.println(ANSI_GREEN + "Logging out..." + ANSI_RESET);
                        return false;  
                    case "6":
                        System.out.println(ANSI_RED + "Exiting the application. Goodbye!" + ANSI_RESET);
                        scanner.close();
                        System.exit(0);
                }
            } catch (Exception e) {
                System.out.println(ANSI_RED + "An unexpected error occurred: " + e.getMessage() + ANSI_RESET);
                e.printStackTrace(); 
            }
        }
    }

    private static void displayUserMenuOptions() {
        System.out.println("1. Show Laptops");
        System.out.println("2. Search Laptop");
        System.out.println("3. Buy Laptop");
        System.out.println("4. Sort Laptops");
        System.out.println("5. Logout");
        System.out.println("6. Exit");
        System.out.print("Choose an option (1-6): ");
    }
}
