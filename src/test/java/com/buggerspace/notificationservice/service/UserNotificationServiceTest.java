package com.buggerspace.notificationservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import com.buggerspace.notificationservice.repository.UserNotificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.buggerspace.notificationservice.entity.Notification;
import com.buggerspace.notificationservice.entity.UserNotification;
import com.buggerspace.notificationservice.service.UserNotificationService;

@RunWith(MockitoJUnitRunner.class)
public class UserNotificationServiceTest {

	@InjectMocks
	private UserNotificationService userNotificationService;

	@Mock
	private UserNotificationRepository mockNotificationRepository;

	@Test
	public void addUserNotification() {
	
		//Given
		String userEmail ="test@gmail.com";
		Long userId = 10L;
		UserNotification userNotification= new UserNotification(userId,userEmail);
		given(mockNotificationRepository.findByUserId(userId)).willReturn(new ArrayList<>(Arrays.asList(userNotification)));
		
		//When
		UserNotification userNotificationFind = userNotificationService.findUserNotificationByUserId(userNotification.getUserId());
		
		//Then
		assertEquals(userNotificationFind.getEmail(),userEmail);
	}
	
	@Test
	public void ensureUserEmailUniqueness() {

	    //Given
		String userEmail ="test@gmail.com";
		Long userId = 10L;
		UserNotification userNotification= new UserNotification(userId,userEmail);
		given(mockNotificationRepository.existsByEmail(userEmail)).willReturn(true);

	    //When
	    RuntimeException exception = assertThrows(RuntimeException.class, () ->
	    userNotificationService.create(userId,userEmail)
	    );

	    //Then
	    assertEquals("There is already a user email as: test@gmail.com", exception.getMessage());

	  }
	
	@Test
	public void addNotification() {

		//Given
		String userEmail ="test@gmail.com";
		Long userId = 10L;
		Long productId = 100L;
		Notification notification = new Notification(productId , "Masa" ,LocalDateTime.now());
		UserNotification userNotification= new UserNotification(userId,userEmail);
		given(mockNotificationRepository.save(userNotification)).willReturn(userNotification);
		given(mockNotificationRepository.findByUserId(userId)).willReturn(new ArrayList<>(Arrays.asList(userNotification)));

		//When
		userNotificationService.addNotificationToUserNotification(userId, notification);

		//Then
		assertEquals(notification,
				userNotificationService.findUserNotificationByUserId(userId).getNotifications().stream().filter(x -> x.getProductId().equals(productId)).findFirst().orElse(null));
	}
	
}
