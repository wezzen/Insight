package com.github.wezzen.insight.service;

import com.github.wezzen.insight.repository.CategoryRepository;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
public class ServiceTestConfiguration {

    @Bean
    @Primary
    public CategoryRepository categoryRepository() {
        return Mockito.mock(CategoryRepository.class);
    }

    @Bean
    @Primary
    public CategoryService categoryService() {
        return new CategoryService(categoryRepository());
    }
}
