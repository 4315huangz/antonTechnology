package org.antontech.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {
    @Mock
    AmazonSimpleEmailService mockSes;
    @InjectMocks
    EmailService emailService;

    @Test
    public void sendEmailTest() {
        emailService.sendEmail("dummy receiver", "dummy subject", "dummy body");
        verify(mockSes).sendEmail(any(SendEmailRequest.class));
    }

    @Test(expected = Exception.class)
    public void sendEmailTest_ExceptionTest() {
        doThrow(Exception.class).when(mockSes.sendEmail(any(SendEmailRequest.class)));
        emailService.sendEmail("dummy receiver", "dummy subject", "dummy body");
    }

    @Test
    public void notifyAntonTechnologyTest() {
        emailService.notifyAntonTechnology( "dummy subject", "dummy body", "dummy receiver");
        verify(mockSes).sendEmail(any(SendEmailRequest.class));
    }

    @Test(expected = Exception.class)
    public void notifyAntonTechnologyTest_ExceptionTest() {
        doThrow(Exception.class).when(mockSes.sendEmail(any(SendEmailRequest.class)));
        emailService.notifyAntonTechnology("dummy subject", "dummy body", "dummy receiver");
    }
}
