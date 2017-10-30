package com.tornyak.noon.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@JsonPropertyOrder({ "status", "contact", "terms-of-service-agreed", "orders" })
public class Account {

	private UUID uuid;

	@JsonProperty("status")
	private Status status;

	@JsonProperty("contact")
	private List<URL> contact;

	@JsonProperty("terms-of-service-agreed")
	private boolean termsOfServiceAgreed;

	@JsonProperty("orders")
	private String orders;

	public enum Status {
		VALID("valid"), DEACTIVATED("deactivated"), REVOKED("revoked");

		private String value;

		private Status(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}
	}

	public Account(UUID uuid, String urlPrefix) {
		this.uuid = uuid;
		this.status = Status.DEACTIVATED;
		this.contact = new ArrayList<>();
		this.orders = urlPrefix + "/acct/" + uuid.toString() + "/orders";
	}

	public UUID getUUID() {
		return uuid;
	}

	public void activate() {
		this.status = Status.VALID;
	}

	public void deactivate() {
		this.status = Status.DEACTIVATED;
	}

	public void revoke() {
		this.status = Status.REVOKED;
	}

	public void updateContactInformation(List<URL> contact) {
		this.contact = new ArrayList<>(contact);
	}
}
