package com.buggerspace.notificationservice.model;

public class ProductStockChangeNotification {

    private Long productId;
    private String productName;
    private Integer stock;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public ProductStockChangeNotification(Long productId, String productName, Integer stock) {
        this.productId = productId;
        this.productName = productName;
        this.stock = stock;
    }
}
