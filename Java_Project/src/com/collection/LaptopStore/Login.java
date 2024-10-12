package com.collection.LaptopStore;

public class Login {
    private static final String ADMIN_USERNAME = "Ragul";
    private static final String ADMIN_PASSWORD = "Ragul";

    private static String username;
    private static String password;

    public static boolean authenticate(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    public static boolean isAdmin(String username, String password) {
        return authenticate(username, password);
    }

    public static String getUsername() {
        return username;
    }

    public static void setCredentials(String username, String password) {
        Login.username = username;
        Login.password = password;
    }

    public static String getPassword() {
        return password;
    }
}
