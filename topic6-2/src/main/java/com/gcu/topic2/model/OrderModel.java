package com.gcu.topic2.model;

public class OrderModel {
    private String orderNo;
    private String productName;
    private int quantity;
    private double price;

    public OrderModel() {}

    public OrderModel(String orderNo, String productName, int quantity, double price) {
        this.orderNo = orderNo;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}