package com.xptosystems.api;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;

import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class WebTestConfig {

    private WebTestConfig() {
    }

    public static MappingJackson2HttpMessageConverter jacksonDateTimeConverter() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    public static ExceptionHandlerExceptionResolver restErrorHandler() {
        final ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(
                    final HandlerMethod handlerMethod, final Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(RestExceptionHandler.class)
                        .resolveMethod(exception);
                if (method != null) {
                    return new ServletInvocableHandlerMethod(new RestExceptionHandler(), method);
                }
                return super.getExceptionHandlerMethod(handlerMethod, exception);
            }
        };
        exceptionResolver.setMessageConverters(Arrays.asList(jacksonDateTimeConverter()));
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }

    public static LocaleResolver fixedLocaleResolver(Locale fixedLocale) {
        return new FixedLocaleResolver(fixedLocale);
    }

    public static SortHandlerMethodArgumentResolver sortArgumentResolver() {
        return new SortHandlerMethodArgumentResolver();
    }

    public static PageableHandlerMethodArgumentResolver pageRequestArgumentResolver() {
        return new PageableHandlerMethodArgumentResolver(sortArgumentResolver());
    }

    public static LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
