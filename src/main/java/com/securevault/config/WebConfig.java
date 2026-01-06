package com.securevault.config;

import com.securevault.service.SessionService;
import com.securevault.service.UserService;
import com.securevault.util.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserService userService;
    private final SessionService sessionService;
    private final JwtUtil jwtUtil;

    public WebConfig(UserService userService,
                     SessionService sessionService,
                     JwtUtil jwtUtil) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ JWT INTERCEPTOR
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                new TokenInterceptor(userService, sessionService, jwtUtil)
        )
        .addPathPatterns("/api/**")
        .excludePathPatterns(
                "/api/auth/**",
                "/api/share/**",
                "/error",
                "/actuator/**"
        );
    }

   @Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOriginPatterns(
                    "https://securevault*.vercel.app"
            )
            .allowedMethods(
                    "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
            )
            .allowedHeaders("*")
            .exposedHeaders("*")
            .allowCredentials(true);
}

}
