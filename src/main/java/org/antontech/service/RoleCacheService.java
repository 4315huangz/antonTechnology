package org.antontech.service;

import com.google.common.cache.LoadingCache;
import org.antontech.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class RoleCacheService {
    @Autowired
    LoadingCache<Long, List<Role>> rolesCache;
    private static final Logger logger = LoggerFactory.getLogger(RoleCacheService.class);

    public List<Role> getRoles(Long userId) {
        logger.info("Checking cache for roles for user ID: {}", userId);
        return rolesCache.getUnchecked(userId);
    }

    public void putRoles(Long userId, List<Role> roles) {
        logger.info("Putting roles into cache for user ID: {}", userId);
        rolesCache.put(userId, roles);
    }

    public void invalidateRoles(Long userId) {
        logger.info("Invalidating cache for user ID: {}", userId);
        rolesCache.invalidate(userId);
    }

    public void clearCache() {
        logger.info("Clearing the entire cache");
        rolesCache.invalidateAll();
    }
}
