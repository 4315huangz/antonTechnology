package org.antontech.filter;

import io.jsonwebtoken.Claims;
import org.antontech.model.Account;
import org.antontech.service.AccountService;
import org.antontech.service.JWTService;
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
import java.util.Set;

@WebFilter(filterName = "SecurityFilter", urlPatterns = "/*", dispatcherTypes = {DispatcherType.REQUEST})
public class SecurityFilter implements Filter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AccountService accountService;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final Set<String> ALLOWED_PATHS = new HashSet<>(Arrays.asList("","login","logout","register"));
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
                Account a = accountService.getAccountById(Long.valueOf(claims.getId()));
                if(a != null) return HttpServletResponse.SC_ACCEPTED;
            }
        } catch (Exception e) {
            logger.info("Cannot get token {}", e.getMessage());
        }
        return statusCode;
    }
}
