package com.tornyak.noon.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "new-nonce", "new-account", "new-order", "new-authz", "revoke-cert", "key-change" })
public final class Directory {

	@JsonProperty("new-nonce")
	private String newNonce;

	@JsonProperty("new-account")
	private String newAccount;

	@JsonProperty("new-order")
	private String newOrder;

	@JsonProperty("new-authz")
	private String newAuthz;

	@JsonProperty("revoke-cert")
	private String revokeCert;

	@JsonProperty("key-change")
	private String keyChange;

	public static class Meta {
		@JsonProperty("terms-of-service")
		private String termsOfService;
		@JsonProperty("website")
		private String website;
		@JsonProperty("caa-identities")
		private String[] caaIdentities;
	}

	public Directory() {
	}

	public Directory(String urlPrefix) {
		newNonce = urlPrefix + "/new-nonce";
		newAccount = urlPrefix + "/new-account";
		newOrder = urlPrefix + "/new-order";
		newAuthz = urlPrefix + "/new-authz";
		revokeCert = urlPrefix + "/revoke-cert";
		keyChange = urlPrefix + "/key-change";
	}

	@Override
	public int hashCode() {
		return Objects.hash(keyChange, newAccount, newAuthz, newNonce, newOrder, revokeCert);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Directory other = (Directory) obj;
		return Objects.equals(this.newNonce, other.newNonce) && Objects.equals(this.newAccount, other.newAccount)
				&& Objects.equals(this.newOrder, other.newOrder) && Objects.equals(this.revokeCert, other.revokeCert)
				&& Objects.equals(this.keyChange, other.keyChange) && Objects.equals(this.newAuthz, other.newAuthz);
	}

	@Override
	public String toString() {
		return "Directory [newNonce=" + newNonce + ", newAccount=" + newAccount + ", newOrder=" + newOrder
				+ ", newAuthz=" + newAuthz + ", revokeCert=" + revokeCert + ", keyChange=" + keyChange + "]";
	}

}
