package com.example.service.handle;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // Extract the username or principal from the request
        String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
        if (StringUtils.isBlank(username)) {
            // Alternatively, if using a custom Authentication filter
            Authentication auth = (Authentication) request.getSession()
                    .getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            if (Objects.nonNull(auth) && Objects.nonNull(auth.getPrincipal()) && auth.getPrincipal() instanceof String) {
                username = (String) auth.getPrincipal();
            }
        }
        if (exception instanceof UsernameNotFoundException) {
            // Handle UsernameNotFoundException
            log.error(username + " not found", exception);
        } else if (exception instanceof BadCredentialsException) {
            // Handle BadCredentialsException
            log.error("Authentication failed", exception);
        } else {
            // Handle other authentication failures
            log.error("Authentication failed", exception);
        }
    }
}
