package com.tornyak.noon;

import java.security.SecureRandom;

import javax.inject.Inject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;

import com.tornyak.noon.model.Directory;
import com.tornyak.noon.nonce.NonceGenerator;

@SpringBootApplication(exclude = { WebSocketAutoConfiguration.class })
@EnableCaching
public class NoonApplication {

	@Inject
	private NoonConfig noonConfig;

	@Bean
	protected Directory directory() {
		return new Directory(noonConfig.getBasepath().toString());
	}
	
	@Bean
	public NonceGenerator nonceGenetator() {
		SecureRandom secureRng = new SecureRandom();
		return new NonceGenerator(secureRng, noonConfig.getNonceSize());
	}

	@Bean
	public FilterRegistrationBean deRegisterHiddenHttpMethodFilter(HiddenHttpMethodFilter filter) {
		FilterRegistrationBean bean = new FilterRegistrationBean(filter);
		bean.setEnabled(false);
		return bean;
	}

	@Bean
	public FilterRegistrationBean deRegisterHttpPutFormContentFilter(HttpPutFormContentFilter filter) {
		FilterRegistrationBean bean = new FilterRegistrationBean(filter);
		bean.setEnabled(false);
		return bean;
	}

	public static void main(String[] args) {
		SpringApplication.run(NoonApplication.class, args);
	}
}
