package com.xptosystems.api.config;

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import lombok.val;

@Configuration
public class ValidatorConfig {

	@Bean
	MethodValidationPostProcessor methodValidationPostProcessor() {
        val processor = new MethodValidationPostProcessor();
        processor.setValidator(validator());
        return processor;
    }

    @Bean
    Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean(name = "messageSource")
    ReloadableResourceBundleMessageSource messageSource() {
        val messageBundle = new ReloadableResourceBundleMessageSource();
        messageBundle.setBasename("classpath:messages/messages");
        messageBundle.setDefaultEncoding("UTF-8");
        return messageBundle;
    }
}
