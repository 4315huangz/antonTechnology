package org.antontech.filter;

import com.google.common.cache.LoadingCache;
import io.jsonwebtoken.Claims;
import org.antontech.model.Role;
import org.antontech.model.User;
import org.antontech.service.RoleCacheService;
import org.antontech.service.JWTService;
import org.antontech.service.RoleService;
import org.antontech.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebFilter(filterName = "SecurityFilter", urlPatterns = "/*", dispatcherTypes = {DispatcherType.REQUEST})
public class SecurityFilter implements Filter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    RoleCacheService roleCacheService;

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final Set<String> IGNORED_PATHS = new HashSet<>((Arrays.asList("/auth")));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("Start to do authorization");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        int statusCode = authorization(req);
        if(statusCode == HttpServletResponse.SC_ACCEPTED) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).sendError(statusCode);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private int authorization(HttpServletRequest req) {
        int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        String uri = req.getRequestURI();
        if (IGNORED_PATHS.contains(uri)) {
            return HttpServletResponse.SC_ACCEPTED;
        }

        try {
            String token = req.getHeader("Authorization").replaceAll("^(.*?) ", "");
            if (token == null || token.isEmpty())
                return statusCode;
            Claims claims = jwtService.decryptToken(token);
            logger.info("===== after parsing JWT token, claims.getId()={}", claims.getId());
            if(claims.getId() != null) {
                User u = userService.getById(Long.valueOf(claims.getId()));
                if(u != null) {
                    List<Role> roles = roleCacheService.getRoles(Long.valueOf(claims.getId()));
                    if (roles == null || roles.isEmpty()) {
                        logger.info("Roles not found in cache. Fetching from the database.");
                        roles = u.getRoles();
                        roleCacheService.putRoles(Long.valueOf(claims.getId()), roles);
                    } else {
                        logger.info("Roles found in cache.");
                    }
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
                    String verb = req.getMethod();
                    String allowedResources = "";
                    switch (verb) {
                        case "GET":
                            allowedResources = allowedReadResources;
                            break;
                        case "POST":
                            allowedResources = allowedCreateResources;
                            break;
                        case "PUT":
                            allowedResources = allowedUpdateResources;
                            break;
                        case "DELETE":
                            allowedResources = allowedDeleteResources;
                    }
                    logger.info("======, allowedResources = {}", allowedResources);
                    if(allowedResources.trim().isEmpty()) return statusCode;
                    for (String s : allowedResources.split(",")) {
                        if (uri.trim().toLowerCase().startsWith(s.trim().toLowerCase())) {
                            statusCode = HttpServletResponse.SC_ACCEPTED;
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.info("Cannot get token {}", e.getMessage());
        }
        return statusCode;
    }
}
