package com.yx.interceptor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration =
                registry.addInterceptor(new LoginInterceptor());
        //拦截信息
        registration.addPathPatterns("/**");
        //不拦截信息
        registration.excludePathPatterns(
                "/loginIn",
                "/**/login.html",
                "/**/*.js",
                "/**/*.css",
                "/**/images/*.*"
        );
        registry.addInterceptor(new TypeInterceptor()).excludePathPatterns(
                "/loginIn",
                "/**.html",
                "/**/*.js",
                "/**/*.css",
                "/**/images/*.*"
        );

    }

    /*@Bean
    @Qualifier(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new XinDispatcherServlet();
    }*/

    @Bean
    public FilterRegistrationBean httpServletRequestReplacedRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestReplacedFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("httpServletRequestReplacedFilter");
        registration.setOrder(1);
        return registration;
    }
}
