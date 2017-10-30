package com.tornyak.noon.model;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@JsonPropertyOrder({ "contact", "terms-of-service-agreed" })
public class NewAccountReq {
	@JsonProperty("contact")
	private List<URI> contact = new ArrayList<>();

	@JsonProperty("terms-of-service-agreed")
	private boolean termsOfServiceAgreed;

	public NewAccountReq() {
	}

	public NewAccountReq(List<URI> contact, boolean termsOfServiceAgreed) {
		if (contact != null) {
			this.contact.addAll(contact);
		}
		this.termsOfServiceAgreed = termsOfServiceAgreed;
	}

	public NewAccountReq(String[] contact, boolean termsOfServiceAgreed) throws MalformedURLException {
		this(stringArrayToUriList(contact), termsOfServiceAgreed);
	}

	private static List<URI> stringArrayToUriList(String[] contact) throws MalformedURLException {
		List<URI> contactUris = new ArrayList<>();
		if (contact != null) {
			for (String s : contact) {
				contactUris.add(URI.create(s));
			}
		}
		return contactUris;
	}

	public List<URI> getContact() {
		return contact;
	}

	public boolean isTermsOfServiceAgreed() {
		return termsOfServiceAgreed;
	}

}
