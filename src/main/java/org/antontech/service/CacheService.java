package org.antontech.service;

import org.antontech.model.User;
import org.antontech.repository.RoleHibernateDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.persistence.Cacheable;
import java.util.List;


@Service
public class CacheService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleHibernateDao roleHibernateDao;

    @Autowired
    private CacheManager cacheManager;
/*
    @Cacheable(value = "identityCache", key = "#userId")
    public List<String> getRoles(User user) {
        return roleHibernateDao.getRolesByUser(user);
    }

 */
}


