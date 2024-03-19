package org.antontech.service;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ResourceCacheService {
    @Autowired
    LoadingCache<Long, Map<String, String>>  allowedResourcesCache;
    private static final Logger logger = LoggerFactory.getLogger(ResourceCacheService.class);

    public Map<String, String> getAllowedResources(Long userId) {
        try {
            return allowedResourcesCache.get(userId);
        } catch (ExecutionException e) {
            logger.error("Error loading data from cache: {}", e.getMessage());
            return Map.of();
        }
    }

    public void invalidateAllowedResources(Long userId) {
        logger.info("Invalidating resource cache for user ID: {}", userId);
        allowedResourcesCache.invalidate(userId);
    }

    public void clearCache() {
        logger.info("Clearing all resource cache");
        allowedResourcesCache.invalidateAll();
    }

    public void logCacheStats() {
        CacheStats stats = allowedResourcesCache.stats();
        logger.info("Cache Stats: Hit Count={}, Miss Count={}, Load Success Count={}, Load Exception Count={}, Total Load Time={}, Eviction Count={}",
                stats.hitCount(), stats.missCount(), stats.loadSuccessCount(), stats.loadExceptionCount(), stats.totalLoadTime(), stats.evictionCount());
    }
}
