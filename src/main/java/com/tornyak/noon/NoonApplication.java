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
	@Value("${noon.acme.host}")
	private String acmeHost;
	@Value("${noon.acme.basepath}")
	private String acmeBasepath;
	
	@Bean
    protected Directory directory() {
        return new Directory(acmeHost + ":" + serverPort + acmeBasepath);
    }

	public static void main(String[] args) {
		SpringApplication.run(NoonApplication.class, args);
	}
}
