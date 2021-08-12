package com.buggerspace.notificationservice.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Document
public class UserNotification {

	@Id
	@Field
	private Long userId;

	@Field
	private String email;

	@Field
	private List<Notification> notifications;

	public UserNotification() {
		
		super();
		this.notifications = new ArrayList<>();
	}

	public UserNotification(Long userId, String email) {
		
		super();
		this.userId = userId;
		this.email = email;
		this.notifications = new ArrayList<>();
	}

	public String getEmail() {
		
		return email;
	}

	public void setEmail(String email) {
		
		this.email = email;
	}

	public Long getUserId() {
		
		return userId;
	}

	public void setUserId(Long userId) {
		
		this.userId = userId;
	}

	public List<Notification> getNotifications() {
		
		return notifications;
	}

	public void setNotifications(Notification notification) {
		
		this.notifications.add(notification);
	}

	@Override
	public String toString() {
		return "UserNotification{" +
				", userId=" + userId +
				", email='" + email + '\'' +
				", notifications=" + notifications +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, email, notifications);
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserNotification that = (UserNotification) o;
		return Objects.equals(userId, that.userId) && Objects.equals(email, that.email) && Objects.equals(notifications, that.notifications);
	}

}
