package com.example.tfood.project531.Model;

import java.util.ArrayList;

public class Order {
    private ArrayList<String> foodNames;
    private int totalQuantities;
    private String totalOrder;
    private String name, phone, address;


    public Order() {
    }

    public Order(ArrayList<String> foodNames, int totalQuantities, String totalOrder, String name, String phone, String address) {
        this.foodNames = foodNames;
        this.totalQuantities = totalQuantities;
        this.totalOrder = totalOrder;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public ArrayList<String> getFoodNames() {
        return foodNames;
    }

    public void setFoodNames(ArrayList<String> foodNames) {
        this.foodNames = foodNames;
    }

    public int getTotalQuantities() {
        return totalQuantities;
    }

    public void setTotalQuantities(int totalQuantities) {
        this.totalQuantities = totalQuantities;
    }

    public String getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(String totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
