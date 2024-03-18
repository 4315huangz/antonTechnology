package org.antontech.service;

import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@Service
public class ResourceCacheService {
    @Autowired
    LoadingCache<Long, Map<String, String>>  allowedResourcesCache;
    private static final Logger logger = LoggerFactory.getLogger(ResourceCacheService.class);

    public Map<String, String> getAllowedResources(Long userId) {
        logger.info("Checking cache for allowed resource for user ID: {}", userId);
        return allowedResourcesCache.getUnchecked(userId);
    }


    public void putAllowedResources(Long userId, Map<String, String> allowedResourcesMap) {
        logger.info("Putting allowed resources into cache for user ID: {}", userId);
        allowedResourcesCache.put(userId, allowedResourcesMap);
    }

    public void invalidateAllowedResources(Long userId) {
        logger.info("Invalidating resource cache for user ID: {}", userId);
        allowedResourcesCache.invalidate(userId);
    }

    public void clearCache() {
        logger.info("Clearing all resource cache");
        allowedResourcesCache.invalidateAll();
    }
}
