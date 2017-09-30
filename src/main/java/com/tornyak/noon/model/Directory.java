package com.tornyak.noon.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@ToString
@EqualsAndHashCode
@Getter
@JsonPropertyOrder({ "new-nonce", "new-account",  "new-order", "new-authz", "revoke-cert", "key-change"})
public final class Directory {

	@JsonProperty("new-nonce")
	private final String newNonce;
	
	@JsonProperty("new-account")
	private final String newAccount;
	
	@JsonProperty("new-order")
	private final String newOrder;
	
	@JsonProperty("new-authz")
	private final String newAuthz;
	
	@JsonProperty("revoke-cert")
	private final String revokeCert;
	
	@JsonProperty("key-change")
	private final String keyChange;
	
	public static class Meta {
		@JsonProperty("terms-of-service")
		private String termsOfService;
		@JsonProperty("website")
		private String website;
		@JsonProperty("caa-identities")
		private String[] caaIdentities;
	}
	
	public Directory(String urlPrefix) {
		newNonce = urlPrefix + "/new-nonce";
		newAccount = urlPrefix + "/new-account";
		newOrder = urlPrefix + "/new-order";
		newAuthz = urlPrefix + "/new-authz";
		revokeCert = urlPrefix + "/revoke-cert";
		keyChange = urlPrefix + "/key-change";
	}
}
