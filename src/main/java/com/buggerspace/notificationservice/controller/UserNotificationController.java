package com.buggerspace.notificationservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buggerspace.notificationservice.entity.Notification;
import com.buggerspace.notificationservice.entity.UserNotification;
import com.buggerspace.notificationservice.service.UserNotificationService;

@RestController
@RequestMapping("/userNotifications")
public class UserNotificationController {
	
	@Autowired
	private UserNotificationService userNotificationService;

	@GetMapping
	public List<UserNotification> getAllNotifications(){
		return userNotificationService.getAllUsersWithNotifications();
	}
	
	@GetMapping("/{id}")
	public List<Notification> getNotifications(@PathVariable("id") Long id) {
		
		return userNotificationService.getNotification(id);
	}

}
