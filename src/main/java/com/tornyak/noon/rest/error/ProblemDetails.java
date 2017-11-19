package com.tornyak.noon.rest.error;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The problem details JSON object as specified in RFC7807
 */
@ToString
@EqualsAndHashCode
@Getter
@JsonPropertyOrder({ "type", "detail", "instance" })
public class ProblemDetails {
	
	@JsonProperty
	private URI type;
	
	@JsonProperty
	private String detail;
	
	@JsonProperty
	private URI instance;
	
	public ProblemDetails(String type, String detail, String instance) {
		this.type = URI.create(type);
		this.detail = detail;
		this.instance = URI.create(instance);
	}

//	    "type": "https://example.com/probs/out-of-credit",
//	    "detail": "Your current balance is 30, but that costs 50.",
//	    "instance": "/account/12345/msgs/abc",
}
