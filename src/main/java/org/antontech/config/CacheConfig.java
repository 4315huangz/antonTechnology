package org.antontech.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.antontech.service.ResourceLoadService;
import org.antontech.service.RoleService;
import org.antontech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig<T> {
    private final long EXPIRE_DURATION = 30;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    ResourceLoadService resourceLoadService;
    @Bean
    public LoadingCache<Long, Map<String, String>> allowedResourcesCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_DURATION, TimeUnit.MINUTES)
                .recordStats()
                .build(new CacheLoader<Long, Map<String, String>>() {
                    @Override
                    public Map<String, String> load(Long userId) throws Exception {
                        return resourceLoadService.loadAllowedResources(userId);
                    }
                });
    }
}
