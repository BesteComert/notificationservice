package com.buggerspace.notificationservice.entity;

import org.springframework.data.couchbase.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Objects;

public class Notification {

	@Field
	private Long productId;
	
	@Field
	private String productName;
	
	@Field
	private LocalDateTime notificationDate;
	
	public Notification(Long productId, String productName, LocalDateTime notificationDate) {

		super();
		this.productId = productId;
		this.productName = productName;
		this.notificationDate = notificationDate;
	}

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

	public LocalDateTime getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(LocalDateTime notificationDate) {
		this.notificationDate = notificationDate;
	}

	@Override
	public String toString() {
		return "Notification [productId=" + productId + ", productName=" + productName
				+ ", notificationDate=" + notificationDate + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Notification that = (Notification) o;
		return Objects.equals(productId, that.productId) && Objects.equals(productName, that.productName) && Objects.equals(notificationDate, that.notificationDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, productName, notificationDate);
	}
}
