package org.antontech.controller;

import org.antontech.service.RoleCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cache")
public class CacheController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    RoleCacheService roleCacheService;

    @PostMapping(value = "/invalidateUser/{id}")
    public void invalidateUserCache(@PathVariable Long userId) {
        roleCacheService.invalidateRoles(userId);
    }

    @PostMapping(value = "/clear")
    public void clearCache() {
        roleCacheService.clearCache();
    }
}
