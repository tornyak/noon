package com.tornyak.noon.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tornyak.noon.model.Account.Status;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@JsonPropertyOrder({ "status", "contact" })
public class NewAccountRsp {

	@JsonProperty("status")
	private Status status;

	@JsonProperty("contact")
	private List<URI> contact = new ArrayList<>();

	public NewAccountRsp() {
	}

	public NewAccountRsp(Status status, List<URI> contact) {
		this.status = status;
		this.contact.addAll(contact);
	}
}
