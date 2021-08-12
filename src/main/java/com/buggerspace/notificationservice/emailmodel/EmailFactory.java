package com.buggerspace.notificationservice.emailmodel;

import com.buggerspace.notificationservice.repository.EmailService;

public class EmailFactory {

    public EmailService create (EmailType emailType){

        EmailService emailService;
        switch (emailType){
            case PRICE:
                emailService = new PriceEmail();
                break;
            case STOCK:
                emailService = new StockEmail();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + emailType);
        }

        return emailService;
    }
}

