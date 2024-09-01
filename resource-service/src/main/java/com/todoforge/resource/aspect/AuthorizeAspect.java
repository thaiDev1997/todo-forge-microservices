package com.todoforge.resource.aspect;

import com.todoforge.resource.aspect.annotation.VerifyClientId;
import com.todoforge.resource.service.SecurityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDeniedException;

import java.lang.reflect.Method;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Aspect
public class AuthorizeAspect {
    private static final String PROPERTY_PATH_REGEX = "^\\$\\{[a-zA-Z0-9._-]+\\}$";

    SecurityService securityService;
    Environment environment;

    private static boolean isPropertyPath(String value) {
        return value != null && value.matches(PROPERTY_PATH_REGEX);
    }

    @Before(value = "@within(com.todoforge.resource.aspect.annotation.VerifyClientId) || @annotation(com.todoforge.resource.aspect.annotation.VerifyClientId)")
    public void verifyClientIdHandler(JoinPoint joinPoint) throws AccessDeniedException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        VerifyClientId verifyClientId = method.getAnnotation(VerifyClientId.class);
        String[] clientIdsInput = verifyClientId.value();
        if (ArrayUtils.isNotEmpty(clientIdsInput)) {
            String[] clientIds = new String[clientIdsInput.length];
            for (int i = 0; i < clientIdsInput.length; i++) {
                String clientId = clientIdsInput[i];
                if (isPropertyPath(clientId)) {
                    String propertyKey = clientId.substring(2, clientId.length() - 1); // Remove ${ and }
                    clientIds[i] = environment.getProperty(propertyKey);
                } else clientIds[i] = clientId;
            }
            if (!securityService.hasClientId(clientIds)) throw new AccessDeniedException("Invalid Client ID");
        }
    }
}
