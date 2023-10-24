package com.example.tfood.project531.Model;

public class Food {

    private String foodName;
    private String foodPic;
    private String description;
    private Double fee;
    private int star;
    private int foodId;
    private int menuId;


    public Food() {
    }

    public Food(String foodName, String foodPic, String description, Double fee, int star, int foodId, int menuId) {
        this.foodName = foodName;
        this.foodPic = foodPic;
        this.description = description;
        this.fee = fee;
        this.star = star;
        this.foodId = foodId;
        this.menuId = menuId;
    }

    public Food(int foodId,String foodName, String foodPic, Double fee) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPic = foodPic;
        this.fee = fee;

    }

    public Food(int foodId, String foodName, String foodPic, Double fee, int menuId) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPic = foodPic;
        this.fee = fee;
        this.menuId = menuId;

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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
}
