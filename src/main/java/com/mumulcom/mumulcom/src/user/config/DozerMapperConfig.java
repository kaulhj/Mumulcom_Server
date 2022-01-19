package com.mumulcom.mumulcom.src.user.config;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozerMapperConfig {

    @Bean
    public Mapper dozerMappper() {
        return DozerBeanMapperBuilder.buildDefault();
    }
}

