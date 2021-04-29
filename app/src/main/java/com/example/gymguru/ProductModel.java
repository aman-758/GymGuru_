package com.example.gymguru;

public class ProductModel {
    public String title;
    public String description;
    public int qty;

    // Creating default constructor important for firebase

    public ProductModel()
    {

    }

    public ProductModel(String title, String description, int qty) {
        this.title = title;
        this.description = description;
        this.qty = qty;
    }
}
