package com.gcu.topic2.model;

import java.math.BigDecimal;

public class OrderModel {
    private String id;
    private String orderNumber;
    private String product;
    private int quantity;
    private BigDecimal price;

    public OrderModel() {
    }

    public OrderModel(String id, String orderNumber, String product, BigDecimal price, int quantity) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
    }

    // --- getters/setters that Thymeleaf/Spring EL need ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}