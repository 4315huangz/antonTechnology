package org.antontech.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private AmazonSimpleEmailService sesClient;
    private final String ANTON_TECHNOLOGY_CONTACT = "antontechnology2022@gmail.com";
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void sendEmail(String to, String subject, String body) {
        SendEmailRequest emailRequest = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(to))
                .withMessage(new Message()
                        .withBody(new Body().withText(new Content().withCharset("UTF-8").withData(body)))
                        .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                .withSource(ANTON_TECHNOLOGY_CONTACT);
        try {
            SendEmailResult result = sesClient.sendEmail(emailRequest);
            logger.info("Email sent successfully to: {}", to);
            logger.info("Message ID: " + result.getMessageId());
        } catch (Exception e) {
            logger.error("The email was not send. Error message: " + e.getMessage());
        }
    }

    public void notifyAntonTechnology(String subject, String body, String from ) {
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(ANTON_TECHNOLOGY_CONTACT))
                .withMessage(new Message()
                        .withBody(new Body().withText(new Content().withCharset("UTF-8").withData(body)))
                        .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                .withSource(from);
        try {
            SendEmailResult result = sesClient.sendEmail(request);
            logger.info("Notification ID: " + result.getMessageId() + " successfully sent to Anton Technology");
        } catch (Exception e) {
            logger.error("The notification was not send. Error message: " + e.getMessage());
        }
    }
}
