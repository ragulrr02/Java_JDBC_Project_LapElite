package com.collection.LaptopStore;

public class Laptop implements Comparable<Laptop> {
    private int id;
    private String name;
    private String brand;
    private String processor;
    private int price;
    private int quantity;

    public Laptop(String name, String brand, String processor, int price, int quantity) {
        this.name = name;
        this.brand = brand;
        this.processor = processor;
        this.price = price;
        this.quantity = quantity;
    }

    public Laptop(int id, String name, String brand, String processor, int price, int quantity) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.processor = processor;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Laptop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", processor='" + processor + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public int compareTo(Laptop otherLaptop) {
        return this.name.compareTo(otherLaptop.getName());
    }
}
