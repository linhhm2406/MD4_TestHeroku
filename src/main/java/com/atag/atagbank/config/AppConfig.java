package com.atag.atagbank.config;

import com.atag.atagbank.formatter.AccountFormatter;
import com.atag.atagbank.formatter.RoleFormatter;
import com.atag.atagbank.service.account.AccountService;
import com.atag.atagbank.service.account.IAccountService;
import com.atag.atagbank.service.role.RoleService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan("com.atag.atagbank.controller")
public class AppConfig implements ApplicationContextAware, WebMvcConfigurer {
    private ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/public/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/public/js/");
        registry.addResourceHandler("/user/js/**").addResourceLocations("classpath:/public/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/public/images/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/public/fonts/");
        registry.addResourceHandler("/scss/**").addResourceLocations("classpath:/public/scss/");
        registry.addResourceHandler("/personal/css/**").addResourceLocations("classpath:/public/personalPage/css/");
        registry.addResourceHandler("/personal/js/**").addResourceLocations("classpath:/public/personalPage/js/");
        registry.addResourceHandler("/personal/images/**").addResourceLocations("classpath:/public/personalPage/images/");
        registry.addResourceHandler("/personal/scss/**").addResourceLocations("classpath:/public/personalPage/scss/");
        registry.addResourceHandler("/user/personal/css/**").addResourceLocations("classpath:/public/personalPage/css/");
        registry.addResourceHandler("/user/personal/js/**").addResourceLocations("classpath:/public/personalPage/js/");
        registry.addResourceHandler("/user/personal/images/**").addResourceLocations("classpath:/public/personalPage/images/");
        registry.addResourceHandler("/user/personal/scss/**").addResourceLocations("classpath:/public/personalPage/scss/");
        registry.addResourceHandler("/admin/personal/css/**").addResourceLocations("classpath:/public/personalPage/css/");
        registry.addResourceHandler("/admin/personal/js/**").addResourceLocations("classpath:/public/personalPage/js/");
        registry.addResourceHandler("/admin/personal/images/**").addResourceLocations("classpath:/public/personalPage/images/");
        registry.addResourceHandler("/admin/personal/scss/**").addResourceLocations("classpath:/public/personalPage/scss/");
        registry.addResourceHandler("/login/css/**").addResourceLocations("classpath:/public/login/css/");
        registry.addResourceHandler("/login/js/**").addResourceLocations("classpath:/public/login/js/");
        registry.addResourceHandler("/transaction/personal/css/**").addResourceLocations("classpath:/public/personalPage/css/");
        registry.addResourceHandler("/transaction/personal/js/**").addResourceLocations("classpath:/public/personalPage/js/");
        registry.addResourceHandler("/transaction/personal/images/**").addResourceLocations("classpath:/public/personalPage/images/");
        registry.addResourceHandler("/transaction/personal/scss/**").addResourceLocations("classpath:/public/personalPage/scss/");

        registry.addResourceHandler("admin/user-update/personal/css/**").addResourceLocations("classpath:/public/personalPage/css/");
        registry.addResourceHandler("admin/user-update/personal/js/**").addResourceLocations("classpath:/public/personalPage/js/");
        registry.addResourceHandler("admin/user-update/personal/images/**").addResourceLocations("classpath:/public/personalPage/images/");
        registry.addResourceHandler("admin/user-update/personal/scss/**").addResourceLocations("classpath:/public/personalPage/scss/");

        registry.addResourceHandler("admin/makeDeposit/personal/css/**").addResourceLocations("classpath:/public/personalPage/css/");
        registry.addResourceHandler("admin/makeDeposit/personal/js/**").addResourceLocations("classpath:/public/personalPage/js/");
        registry.addResourceHandler("admin/makeDeposit/personal/images/**").addResourceLocations("classpath:/public/personalPage/images/");
        registry.addResourceHandler("admin/makeDeposit/personal/scss/**").addResourceLocations("classpath:/public/personalPage/scss/");
        registry.addResourceHandler("admin/active/personal/css/**").addResourceLocations("classpath:/public/personalPage/css/");
        registry.addResourceHandler("admin/active/personal/js/**").addResourceLocations("classpath:/public/personalPage/js/");
        registry.addResourceHandler("admin/active/personal/images/**").addResourceLocations("classpath:/public/personalPage/images/");
        registry.addResourceHandler("admin/active/personal/scss/**").addResourceLocations("classpath:/public/personalPage/scss/");

        registry.addResourceHandler("admin/deactive/personal/css/**").addResourceLocations("classpath:/public/personalPage/css/");
        registry.addResourceHandler("admin/deactive/personal/js/**").addResourceLocations("classpath:/public/personalPage/js/");
        registry.addResourceHandler("admin/deactive/personal/images/**").addResourceLocations("classpath:/public/personalPage/images/");
        registry.addResourceHandler("admin/deactive/personal/scss/**").addResourceLocations("classpath:/public/personalPage/scss/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new AccountFormatter(appContext.getBean(AccountService.class)));
        registry.addFormatter(new RoleFormatter(appContext.getBean(RoleService.class)));
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        registry.addInterceptor(interceptor);
        interceptor.setParamName("lang");
    }
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("en"));
        return localeResolver;
    }
}
