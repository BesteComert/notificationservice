package com.buggerspace.notificationservice.emailmodel;

import com.buggerspace.notificationservice.repository.EmailService;
import com.buggerspace.notificationservice.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
public class StockEmail implements EmailService {

    private final MailService mailService;

    public StockEmail(){
        this.mailService = new MailService();
    }

    @Override
    public void sendEmail(EmailModel emailModel) {
        mailService.sendFromGMail(emailModel.getUserEmail(), emailModel.getTopic(),
                emailModel.getBody());
    }
}
