package codenomads.tripmanagement.jwt.interceptor;


import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import codenomads.tripmanagement.exception.CustomException;
import codenomads.tripmanagement.jwt.JwtUtils;
import codenomads.tripmanagement.jwt.annotation.SkipToken;

import java.lang.reflect.Method;

@Aspect
@Component

public class LogInterceptor {

    @Resource
    private JwtUtils jwtUtils;

    @Around("execution(* codenomads.tripmanagement.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // Get request
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        // Get method
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        boolean isSkip = method.isAnnotationPresent(SkipToken.class) &&
                method.getAnnotation(SkipToken.class).required();

        if (!isSkip) {
            // Authenticate JWT
            String token = getJwtTokenFromRequest(httpServletRequest);
            if (token == null || !jwtUtils.validateToken(token)) {
                throw new CustomException("Not Authorized", 401);
            }
            // store jwt and userid into request
            requestAttributes.setAttribute("jwt", token, RequestAttributes.SCOPE_REQUEST);
            requestAttributes.setAttribute("userid", jwtUtils.extractUserId(token), RequestAttributes.SCOPE_REQUEST);
        }

        try {
            // proceed with original method
            Object result = point.proceed();
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

