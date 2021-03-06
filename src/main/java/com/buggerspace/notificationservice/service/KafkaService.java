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
                emailModel.setTopic("Sepetine ekledi??in ??r??n??n fiyat?? d????t??!");
                emailModel.setBody("Sepetindeki " + productPriceChangeNotification.getProductName() + " ??r??n??n??n " +
                        "fiyat?? d????t??!");

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
                    emailModel.setTopic("Sepetine ekledi??in ??r??n t??kendi!");
                    emailModel.setBody("Sepetindeki " + productStockChangeNotification.getProductName() + " ??r??n?? " +
                            "t??kendi, fakat yenilenen stoklarda sana bildirim g??nderiyor olaca????z!");
                } else if (productStockChangeNotification.getStock() <= 3) {
                    emailModel.setTopic("Sepetine ekledi??in ??r??n t??kenmek ??zere!");
                    emailModel.setBody("Sepetindeki " + productStockChangeNotification.getProductName() + " ??r??n?? " +
                            "t??kenmek ??zere, son birka?? ??r??n i??in uygulamam??z?? ziyaret etmeyi unutma!");
                }

                EmailService stockEmail = new EmailFactory().create(EmailType.STOCK);
                stockEmail.sendEmail(emailModel);
            }
        }
    }
}
