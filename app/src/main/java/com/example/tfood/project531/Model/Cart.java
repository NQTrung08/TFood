package com.example.tfood.project531.Model;

public class Cart {
    private String  foodName, foodPic;
    private Double fee;
    private int quantity, foodId;
    private Double totalPrice;

    public Cart() {
    }

    public Cart(int foodId, String foodName, String foodPic, Double fee, int quantity, Double totalPrice) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPic = foodPic;
        this.fee = fee;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPic() {
        return foodPic;
    }

    public void setFoodPic(String foodPic) {
        this.foodPic = foodPic;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
