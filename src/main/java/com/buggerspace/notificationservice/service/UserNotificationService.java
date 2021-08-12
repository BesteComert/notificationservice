package com.buggerspace.notificationservice.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buggerspace.notificationservice.entity.Notification;
import com.buggerspace.notificationservice.entity.UserNotification;
import com.buggerspace.notificationservice.repository.UserNotificationRepository;

@Service
public class UserNotificationService {

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    public UserNotification create(UserNotification userNotification) {

        if (userNotificationRepository.existsByEmail(userNotification.getEmail())) {
            throw new RuntimeException("There is already a user email as: " + userNotification.getEmail());
        }

        return userNotificationRepository.save(userNotification);
    }

    public UserNotification create(Long userId, String email) {

        if (userNotificationRepository.existsByEmail(email)) {
            throw new RuntimeException("There is already a user email as: " + email);
        }

        return userNotificationRepository.save(new UserNotification(userId, email));
    }

    public void deleteUserNotificationByEmail(String email) {
        userNotificationRepository.deleteByEmail(email);
    }


    public UserNotification findUserNotificationByUserId(Long id) {

        return userNotificationRepository.findByUserId(id).stream().findFirst().orElse(new UserNotification(id,
                "dbcomert@hotmail" +
                ".com"));
    }

    public void addNotificationToUserNotification(Long userId, Long productId, String productName,
                                                  LocalDateTime notificationDate) {

        UserNotification userNotification = findUserNotificationByUserId(userId);

        Notification notification = new Notification(productId, productName, notificationDate);
        userNotification.setNotifications(notification);

        try{
            userNotificationRepository.deleteById(userId);
        }
        finally {
            userNotificationRepository.save(userNotification);
        }

    }

    public void addNotificationToUserNotification(Long userId, Notification notification) {

        UserNotification userNotification = findUserNotificationByUserId(userId);

        userNotification.setNotifications(notification);

        try{
            userNotificationRepository.deleteById(userId);
        }
        finally {
            userNotificationRepository.save(userNotification);
        }
    }

    public List<Notification> getNotification(Long userId) {

        UserNotification userNotification = findUserNotificationByUserId(userId);
        return userNotification.getNotifications();
    }

    public List<UserNotification> getAllUsersWithNotifications() {

        return userNotificationRepository.findAll();
    }

    public boolean checkDuration(Long userId) {

        UserNotification userNotification = findUserNotificationByUserId(userId);
        Notification notificationLast =
                userNotification.getNotifications().get(userNotification.getNotifications().size() - 1);
        Duration duration = Duration.between(notificationLast.getNotificationDate(),
                LocalDateTime.now());
        return duration.getSeconds() >= 3600;
    }
}
