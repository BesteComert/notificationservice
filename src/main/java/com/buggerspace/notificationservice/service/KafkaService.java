package com.buggerspace.notificationservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.buggerspace.notificationservice.emailmodel.EmailFactory;
import com.buggerspace.notificationservice.emailmodel.EmailModel;
import com.buggerspace.notificationservice.emailmodel.EmailType;
import com.buggerspace.notificationservice.model.ProductStockChangeNotification;
import com.buggerspace.notificationservice.model.ProductPriceChangeNotification;
import com.buggerspace.notificationservice.repository.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.buggerspace.notificationservice.entity.Notification;
import com.buggerspace.notificationservice.entity.UserNotification;
import org.springframework.web.client.RestTemplate;

@Service
public class KafkaService {

    @Autowired
    UserNotificationService userNotificationService;

    EmailService emailService;

    public RestTemplate restTemplate;

    @KafkaListener(
            topics = "${spring.kafka.topic.product-price-change-notification}",
            groupId = "product-price-change-group",
            clientIdPrefix = "product-price-change-group",
            containerFactory = "productPriceChangeQueueKafkaListenerContainerFactory"
    )
    public void getNotification(ProductPriceChangeNotification productPriceChangeNotification) {

        Long productId = productPriceChangeNotification.getProductId();
        List<Long> userIds = restTemplate.getForObject("http://localhost:9091/product/"
              + productId, List.class);

        for (Long userId : userIds) {

            Notification notification = new Notification(productId, productPriceChangeNotification.getProductName(),
                    LocalDateTime.now());
            UserNotification userNotification = userNotificationService.findUserNotificationByUserId(userId);

            if (userNotificationService.checkDuration(userId)) {
                userNotificationService.addNotificationToUserNotification(userId, notification);


                EmailModel emailModel = new EmailModel();
                emailModel.setUserEmail(userNotification.getEmail());
                emailModel.setTopic("Sepetine eklediğin ürünün fiyatı düştü!");
                emailModel.setBody("Sepetindeki " + productPriceChangeNotification.getProductName() + " ürününün " +
                        "fiyatı düştü!");

                EmailService priceEmail = new EmailFactory().create(EmailType.PRICE);

                priceEmail.sendEmail(emailModel);
            }
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.product-stock-change-notification}",
            groupId = "product-stock-change-group",
            clientIdPrefix = "product-stock-change-group",
            containerFactory = "productStockChangeQueueKafkaListenerContainerFactory"
    )
    public void getStockNotification(ProductStockChangeNotification productStockChangeNotification) {

        Long productId = productStockChangeNotification.getProductId();
        List<Long> userIds = restTemplate.getForObject("http://localhost:9091/product/"
                + productId, List.class);

        for (Long userId : userIds) {

            Notification notification = new Notification(productId, productStockChangeNotification.getProductName(),
                    LocalDateTime.now());
            UserNotification userNotification = userNotificationService.findUserNotificationByUserId(userId);

            if (userNotificationService.checkDuration(userId)) {
                userNotificationService.addNotificationToUserNotification(userId, notification);

                EmailModel emailModel = new EmailModel();
                emailModel.setUserEmail(userNotification.getEmail());

                if (productStockChangeNotification.getStock() == 0) {
                    emailModel.setTopic("Sepetine eklediğin ürün tükendi!");
                    emailModel.setBody("Sepetindeki " + productStockChangeNotification.getProductName() + " ürünü " +
                            "tükendi, fakat yenilenen stoklarda sana bildirim gönderiyor olacağız!");
                } else if (productStockChangeNotification.getStock() <= 3) {
                    emailModel.setTopic("Sepetine eklediğin ürün tükenmek üzere!");
                    emailModel.setBody("Sepetindeki " + productStockChangeNotification.getProductName() + " ürünü " +
                            "tükenmek üzere, son birkaç ürün için uygulamamızı ziyaret etmeyi unutma!");
                }

                EmailService stockEmail = new EmailFactory().create(EmailType.STOCK);
                stockEmail.sendEmail(emailModel);
            }
        }
    }
}
