package com.tornyak.noon;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.tornyak.noon.model.Directory;

@SpringBootApplication
public class NoonApplication {
	@Value("${server.port}")
	private String serverPort;
	@Value("${noon.acme.baseurl}")
	private String acmeBaseUrl;
	
	@Bean
    protected Directory stormtrooperDao() {
        return new Directory(acmeBaseUrl + ":" + serverPort);
    }

	public static void main(String[] args) {
		SpringApplication.run(NoonApplication.class, args);
	}
}
