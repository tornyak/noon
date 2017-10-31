package com.tornyak.noon.rest.jws;

import java.security.Key;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

public class JwsAcmeBuilder {

	private JsonWebSignature jws;
	private JwsAcmeHeader header;
	private byte[] payloadBytes;
	private Key key;

	public JwsAcmeBuilder jws(JsonWebSignature jws) {
		this.jws = jws;
		return this;
	}
	
	public JwsAcmeBuilder header(JwsAcmeHeader header) {
		this.header = header;
		return this;
	}

	public JwsAcmeBuilder payload(byte[] payloadBytes) {
		this.payloadBytes = payloadBytes;
		return this;
	}

	public JwsAcmeBuilder key(Key key) {
		this.key = key;
		return this;
	}

	public JwsAcme signed() throws JoseException {
		JwsAcme jwsAcme = build();
		jwsAcme.sign();
		return jwsAcme;
	}
	
	public JwsAcme unsigned() {
		return build();
	}
	
	private JwsAcme build() {
		if(jws == null) {
			jws = new JsonWebSignature();
		}
		setHeader();
		setPayload();
		setKey();
		return new JwsAcme(jws);
	}
	
	private void setHeader() {
		jws.setAlgorithmHeaderValue(header.getAlgorithm());
		jws.setHeader("nonce", header.getNonce().toString());
		jws.setHeader("url", header.getUrl().toString());
		if (header.getJwk() != null) {
			jws.getHeaders().setJwkHeaderValue("jwk", header.getJwk());
		} else {
			jws.setHeader("kid", header.getKeyId().toString());
		}
	}
	
	public void setPayload() {
		jws.setPayloadBytes(payloadBytes);
	}
	
	public void setKey() {
		jws.setKey(key);
	}


}
