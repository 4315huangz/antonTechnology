package org.antontech.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private AmazonSimpleEmailService sesClient;
    private String sender = "ziweih1993@gmail.com";
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void sendEmail(String to, String subject, String body) {
        SendEmailRequest emailRequest = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(to))
                .withMessage(new Message()
                        .withBody(new Body().withText(new Content().withCharset("UTF-8").withData(body)))
                        .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                .withSource(sender);
        try {
            SendEmailResult result = sesClient.sendEmail(emailRequest);
            logger.info("Email sent successfully to: {}", to);
            logger.info("Message ID: " + result.getMessageId());
        } catch (Exception e) {
            logger.error("The email was not send. Error message: " + e.getMessage());
        }
    }

}
