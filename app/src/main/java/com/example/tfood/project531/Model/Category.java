package com.example.tfood.project531.Model;

public class Category {
    private String categoryName;
    private String categoryPic;

    public Category() {
    }

    public Category(String categoryName, String categoryPic) {
        this.categoryName = categoryName;
        this.categoryPic = categoryPic;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setTitle(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPic() {
        return categoryPic;
    }

    public void setPic(String categoryPic) {
        this.categoryPic = categoryPic;
    }
}
