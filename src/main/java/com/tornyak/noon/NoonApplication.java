package com.tornyak.noon;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;

import com.tornyak.noon.model.Directory;

@SpringBootApplication(exclude = { WebSocketAutoConfiguration.class })
public class NoonApplication {
	@Value("${server.port}")
	private String serverPort;
	@Value("${noon.acme.host}")
	private String acmeHost;
	@Value("${noon.acme.basepath}")
	private String acmeBasepath;

	@Bean
	protected Directory directory() {
		return new Directory(acmeHost + ":" + serverPort + acmeBasepath);
	}

	@Bean
	public FilterRegistrationBean deRegisterHiddenHttpMethodFilter(HiddenHttpMethodFilter filter) {
		FilterRegistrationBean bean = new FilterRegistrationBean(filter);
		bean.setEnabled(false);
		return bean;
	}

	@Bean
	FilterRegistrationBean deRegisterHttpPutFormContentFilter(HttpPutFormContentFilter filter) {
		FilterRegistrationBean bean = new FilterRegistrationBean(filter);
		bean.setEnabled(false);
		return bean;
	}

	public static void main(String[] args) {
		SpringApplication.run(NoonApplication.class, args);
	}
}
