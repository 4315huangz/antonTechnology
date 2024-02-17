package org.antontech.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class AWSConfig {
    private Regions defaultRegion = Regions.US_EAST_1;
    @Bean
    public AmazonS3 getAmazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(defaultRegion)
                .build();
    }

    @Bean
    public AmazonSimpleEmailService getAmazonSES() {
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(defaultRegion)
                .build();
    }
}
