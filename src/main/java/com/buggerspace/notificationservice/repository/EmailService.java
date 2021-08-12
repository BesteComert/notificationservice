package com.buggerspace.notificationservice.repository;

import com.buggerspace.notificationservice.emailmodel.EmailModel;

public interface EmailService {

    void sendEmail(EmailModel emailModel);
}
