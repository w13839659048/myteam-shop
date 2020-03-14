package com.qf.config;

import com.qf.interceptor.AuthInterceptor1;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.Authenticator;

@Configuration
public class WeConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor1 authInterceptor1;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor1).addPathPatterns("/**");
    }
}
