package org.antontech.service;

import org.antontech.model.Role;
import org.antontech.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceLoadService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;

    public Map<String, String> loadAllowedResources(Long userId) {
        User user = userService.getUserSecurityById(userId);
        if(user != null) {
            Map<String, String> allowedResourcesMap = new HashMap<>();
            List<Role> roles = user.getRoles();
            String allowedReadResources = "";
            String allowedCreateResources = "";
            String allowedUpdateResources = "";
            String allowedDeleteResources = "";
            for (Role role : roles) {
                if(roleService.getAllowedReadResources(role) != null)
                    allowedReadResources = String.join(roleService.getAllowedReadResources(role), allowedReadResources, ",");
                if(roleService.getAllowedCreateResources(role) != null)
                    allowedCreateResources = String.join(roleService.getAllowedCreateResources(role), allowedCreateResources, ",");
                if(roleService.getAllowedUpdateResources(role) != null)
                    allowedUpdateResources = String.join(roleService.getAllowedUpdateResources(role), allowedUpdateResources, ",");
                if(roleService.getAllowedDeleteResources(role) != null)
                    allowedDeleteResources = String.join(roleService.getAllowedDeleteResources(role), allowedDeleteResources, ",");
            }
            logger.info("======, allowedReadResources = {}", allowedReadResources);
            logger.info("======, allowedCreateResources = {}", allowedCreateResources);
            logger.info("======, allowedUpdateResources = {}", allowedUpdateResources);
            logger.info("======, allowedDeleteResources = {}", allowedDeleteResources);
            allowedResourcesMap.put("allowedReadResources", allowedReadResources);
            allowedResourcesMap.put("allowedCreateResources", allowedCreateResources);
            allowedResourcesMap.put("allowedUpdateResources", allowedUpdateResources);
            allowedResourcesMap.put("allowedDeleteResources", allowedDeleteResources);
            return allowedResourcesMap;
        } else
            return Collections.emptyMap();
    }
}
