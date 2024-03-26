package org.antontech.filter;

import io.jsonwebtoken.Claims;
import org.antontech.service.ResourceCacheService;
import org.antontech.service.JWTService;
import org.antontech.service.ResourceLoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@WebFilter(filterName = "SecurityFilter", urlPatterns = "/*", dispatcherTypes = {DispatcherType.REQUEST})
public class SecurityFilter implements Filter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private ResourceCacheService resourceCacheService;

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final Set<String> IGNORED_PATHS = new HashSet<>((Arrays.asList("/auth")));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (jwtService == null) {
            SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, servletRequest.getServletContext());
        }
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
                long userId = Long.valueOf(claims.getId());
                HttpSession session = req.getSession(true);
                session.setAttribute("loggedInUserId", userId);
                Map<String, String> allowedResourcesMap = resourceCacheService.getAllowedResources(userId);
                resourceCacheService.logCacheStats();

                String allowedReadResources = allowedResourcesMap.get("allowedReadResources");
                String allowedCreateResources = allowedResourcesMap.get("allowedCreateResources");;
                String allowedUpdateResources = allowedResourcesMap.get("allowedUpdateResources");;
                String allowedDeleteResources = allowedResourcesMap.get("allowedDeleteResources");;
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
                    case "PATCH":
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
        } catch (Exception e) {
            logger.info("Cannot get token {}", e.getMessage());
        }
        return statusCode;
    }
}
