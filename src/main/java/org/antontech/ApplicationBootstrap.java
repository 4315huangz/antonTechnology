package org.antontech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"org.antontech"})
@ServletComponentScan(basePackages = {"org.antontech.filter"})
public class ApplicationBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationBootstrap.class, args);
    }
}
