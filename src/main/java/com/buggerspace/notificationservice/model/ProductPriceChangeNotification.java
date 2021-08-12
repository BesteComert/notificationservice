package com.buggerspace.notificationservice.model;

public class ProductPriceChangeNotification {
	
	private Long productId;
	private String productName;
	
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

	public ProductPriceChangeNotification(Long productId, String productName) {

		this.productId = productId;
		this.productName = productName;
	}
}
