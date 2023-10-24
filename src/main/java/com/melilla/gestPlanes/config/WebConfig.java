package com.melilla.gestPlanes.config;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.domain.AuditorAware;

import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.transaction.annotation.EnableTransactionManagement;


import com.melilla.gestPlanes.envers.AuditorAwareImpl;





@Configuration
@ComponentScan(basePackages = {"com.melilla.gestPlanes","com.melilla.gestPlanes.repository"})
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaRepositories(basePackages = {"com.melilla.gestPlanes"},repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@EnableTransactionManagement
public class WebConfig {


	@Bean
	AuditorAware<String> auditorProvider(){
		return new AuditorAwareImpl();
	}
	
	
}
