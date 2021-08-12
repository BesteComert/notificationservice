package com.buggerspace.notificationservice.emailmodel;

import com.buggerspace.notificationservice.repository.EmailService;
import com.buggerspace.notificationservice.service.MailService;

public class PriceEmail implements EmailService {

    private final MailService mailService;

    public PriceEmail(){
        this.mailService = new MailService();
    }

    @Override
    public void sendEmail(EmailModel emailModel) {
        mailService.sendFromGMail(emailModel.getUserEmail(), emailModel.getTopic(),
                 emailModel.getBody());
    }
}
