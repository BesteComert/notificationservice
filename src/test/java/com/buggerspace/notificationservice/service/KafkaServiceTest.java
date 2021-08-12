package com.buggerspace.notificationservice.service;

import com.buggerspace.notificationservice.emailmodel.PriceEmail;
import com.buggerspace.notificationservice.entity.UserNotification;
import com.buggerspace.notificationservice.model.ProductPriceChangeNotification;
import com.buggerspace.notificationservice.model.ProductStockChangeNotification;
import com.buggerspace.notificationservice.repository.EmailService;
import com.buggerspace.notificationservice.service.KafkaService;
import com.buggerspace.notificationservice.service.MailService;
import com.buggerspace.notificationservice.service.UserNotificationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class KafkaServiceTest {

    @InjectMocks
    KafkaService kafkaService;

    @Mock
    UserNotificationService mockUserNotificationService;

    @Mock
    RestTemplate mockRestTemplate;

    @Spy
    EmailService emailService;

    @Spy
    PriceEmail priceEmail;

    @Spy
    MailService mailService;

    @Test
    public void sendPriceEmail() {

        //GIVEN
        Long userId = 101L;
        List<Long> mockUserList = new ArrayList();
        mockUserList.add(userId);

        Long productId = 103L;

        ProductPriceChangeNotification productPriceChangeNotification = new ProductPriceChangeNotification(productId,
                "Macbook Air");
        given(mockUserNotificationService.findUserNotificationByUserId(userId)).willReturn(new UserNotification(userId,
                "dbcomert@hotmail.com"));


        given(mockUserNotificationService.checkDuration(userId)).willReturn(true);
        given(mockRestTemplate.getForObject("http://localhost:9091/product/"
                + productId, List.class)).willReturn(mockUserList);

        //WHEN
        kafkaService.getNotification(productPriceChangeNotification);
    }

    @Test
    public void sendStockEmail() {

        Long userId = 101L;
        List<Long> mockUserList = new ArrayList();
        mockUserList.add(userId);

        Long productId = 103L;

        ProductStockChangeNotification productStockChangeNotification = new ProductStockChangeNotification(productId,
                "Macbook Air", 2);
        given(mockUserNotificationService.findUserNotificationByUserId(userId)).willReturn(new UserNotification(userId,
                "dbcomert@hotmail.com"));


        given(mockUserNotificationService.checkDuration(userId)).willReturn(true);
        given(mockRestTemplate.getForObject("http://localhost:9091/product/"
                + productId, List.class)).willReturn(mockUserList);

        //WHEN
        kafkaService.getStockNotification(productStockChangeNotification);
    }

    @Test
    public void sendStockSoldOutEmail() {

        Long userId = 101L;
        List<Long> mockUserList = new ArrayList();
        mockUserList.add(userId);

        Long productId = 103L;

        ProductStockChangeNotification productStockChangeNotification = new ProductStockChangeNotification(productId,
                "Macbook Air", 0);
        given(mockUserNotificationService.findUserNotificationByUserId(userId)).willReturn(new UserNotification(userId,
                "dbcomert@hotmail.com"));


        given(mockUserNotificationService.checkDuration(userId)).willReturn(true);
        given(mockRestTemplate.getForObject("http://localhost:9091/product/"
                + productId, List.class)).willReturn(mockUserList);

        //WHEN
        kafkaService.getStockNotification(productStockChangeNotification);
    }
}
