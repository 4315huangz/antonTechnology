package org.antontech.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.antontech.dto.UserDTO;
import org.antontech.model.Role;
import org.antontech.model.User;
import org.antontech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig<T> {
    private final long EXPIRE_DURATION = 30;
    @Autowired
    UserService userService;

    @Bean
    public LoadingCache<Long, List<Role>> rolesCache () {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_DURATION, TimeUnit.MINUTES)
                .build(new CacheLoader<Long, List<Role>>() {
                            @Override
                            public List<Role> load(Long userId) throws Exception {
                                User user = userService.getUserSecurityById(userId);
                                return user != null ? user.getRoles() : null;
                            }
                        });
    }
}
