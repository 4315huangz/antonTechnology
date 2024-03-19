package org.antontech.service;

import com.google.common.cache.LoadingCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResourceCacheServiceTest {
    @Mock
    LoadingCache<Long, Map<String, String>> mockAllowedResourceCache;

    @InjectMocks
    ResourceCacheService resourceCacheService;

    @Test
    public void getAllowedResourcesTest() {
        Map<String, String> map = new HashMap<>();
        map.put("allowed", "Test resource");
        when(mockAllowedResourceCache.getUnchecked(anyLong())).thenReturn(map);
        Map<String, String> actual = resourceCacheService.getAllowedResources(1L);
        assertEquals(map, actual);
    }

    @Test
    public void putAllowedResourcesTest() {
        Map<String, String> map = new HashMap<>();
        map.put("allowed", "Test resource");
        resourceCacheService.putAllowedResources(1L, map);
        verify(mockAllowedResourceCache).put(1L, map);
    }

    @Test
    public void invalidateAllowedResourcesTest() {
        resourceCacheService.invalidateAllowedResources(anyLong());
        verify(mockAllowedResourceCache).invalidate(anyLong());
    }

    @Test
    public void clearCacheTest() {
        resourceCacheService.clearCache();
        verify(mockAllowedResourceCache).invalidateAll();
    }
}
