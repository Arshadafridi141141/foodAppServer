package com.example.admin_server.Model;

public class Order {
    public String ProductID;
    public String ProductName;
    public String Quantity;
    public String Price;
    public String Discount;


    public Order(){}

    public Order(String productId, String productName, String quantity, String price, String discount) {
        ProductID = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
    }

    public String getProductId() {
        return ProductID;
    }

    public void setProductId(String productId) {
        ProductID = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
