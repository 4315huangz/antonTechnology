package org.antontech.filter;

import io.jsonwebtoken.Claims;
import org.antontech.model.Role;
import org.antontech.model.User;
import org.antontech.service.JWTService;
import org.antontech.service.RoleService;
import org.antontech.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebFilter(filterName = "SecurityFilter", urlPatterns = "/*", dispatcherTypes = {DispatcherType.REQUEST})
public class SecurityFilter implements Filter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

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
                    String allowedReadResources = "";
                    String allowedCreateResources = "";
                    String allowedUpdateResources = "";
                    String allowedDeleteResources = "";
                    List<Role> roles = u.getRoles();
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
