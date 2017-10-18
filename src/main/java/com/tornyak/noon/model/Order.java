package com.tornyak.noon.model;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonPropertyOrder({ "status", "expires", "csr", "notBefore", "notAfter", "authorizations", "certificate" })
public class Order {

	private static final Logger LOGGER = LoggerFactory.getLogger(Order.class);
	
	private UUID uuid;
	
	@JsonProperty("status")
	private Status status;
	
	@JsonProperty("expires")
	private  LocalDateTime expires;
	
	@JsonProperty("csr")
	private String csr;
	
	@JsonProperty("notBefore")
	private  LocalDateTime notBefore;
	
	@JsonProperty("notAfter")
	private  LocalDateTime notAfter;
	
	@JsonProperty("authorizations")
	private Set<URL> authorizationUrls;
	
	@JsonProperty("certificate")
	private URL certificateUrl;
	
	
	public enum Status {
		PENDING("pending"), PROCESSING("processing"), VALID("valid");
		
		private String value;
		private Status(String value) {
			this.value = value;
		}
		
		@JsonValue
		public String getValue() {
			return value;
		}		
	}
	
	public Order(UUID uuid, String urlPrefix, Status status, String csr) {
		this.uuid = uuid;
		this.status = status;
		this.csr = csr;
		this.authorizationUrls = new HashSet<>();
	}
	
	public Order() {
		
	}
	
	public void process() {
		// TODO
	}
}
