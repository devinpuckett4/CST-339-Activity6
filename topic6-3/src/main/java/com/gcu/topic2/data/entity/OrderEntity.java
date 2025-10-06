// src/main/java/com/gcu/topic2/data/entity/OrderEntity.java
package com.gcu.topic2.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class OrderEntity {
    @Id
    private String id;

    private String orderNumber;  // e.g., "A100"
    private String product;      // e.g., "Coffee Beans"
    private int quantity;        // e.g., 2
    private double price;        // e.g., 14.99

    public OrderEntity() {}

    // --- Canonical getters/setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    // --- Compatibility aliases (used by older code) ---

    public String getOrderNo() { return orderNumber; }
    public void setOrderNo(String orderNo) { this.orderNumber = orderNo; }

    public String getProductName() { return product; }
    public void setProductName(String productName) { this.product = productName; }
}