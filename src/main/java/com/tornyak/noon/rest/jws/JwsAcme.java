package com.tornyak.noon.rest.jws;

import java.net.MalformedURLException;
import java.net.URI;
import java.security.Key;
import java.util.Objects;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tornyak.noon.nonce.Nonce;
import com.tornyak.noon.rest.jws.JwsAcmeHeader.JwsAcmeHeaderBuilder;

@JsonDeserialize(using = JwsAcmeDeserializer.class)
public final class JwsAcme {

	private JsonWebSignature jws;

	protected JwsAcme() {
		jws = new JsonWebSignature();
	}

	protected JwsAcme(JsonWebSignature jws) {
		this.jws = jws;
	}

	public JwsAcmeHeader getHeader() throws MalformedURLException, JoseException {
		JwsAcmeHeaderBuilder builder = JwsAcmeHeader.builder();

		String algorithm = jws.getAlgorithmHeaderValue();
		String nonce = jws.getHeaders().getStringHeaderValue("nonce");
		String url = jws.getHeaders().getStringHeaderValue("url");
		String kid = jws.getHeaders().getStringHeaderValue("kid");

		builder.algorithm(algorithm).nonce(Nonce.fromBase64UrlString(nonce)).url(URI.create(url).toURL());
		if (kid != null) {
			builder.kid(URI.create(kid).toURL());
		} else {
			builder.jwk(jws.getHeaders().getJwkHeaderValue("jwk"));
		}

		return builder.build();
	}

	public String getEncodedHeader() {
		return jws.getHeaders().getEncodedHeader();
	}

	public String getEncodedPayload() {
		return jws.getEncodedPayload();
	}

	public String getUnverifiedPayload() {
		return jws.getUnverifiedPayload();
	}

	public String getPayload() throws JoseException {
		return jws.getPayload();
	}

	public byte[] getPayloadBytes() throws JoseException {
		return jws.getPayloadBytes();
	}

	public String getEncodedSignature() {
		return jws.getEncodedSignature();
	}

	public void setKey(Key key) {
		jws.setKey(key);
	}

	public void sign() throws JoseException {
		jws.sign();
	}

	public boolean isSignatureValid() throws JoseException {
		return jws.verifySignature();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}

		JwsAcme otherJws = (JwsAcme) other;
		return Objects.equals(getEncodedHeader(), otherJws.getEncodedHeader())
				&& Objects.equals(getEncodedPayload(), otherJws.getEncodedPayload())
				&& Objects.equals(getEncodedSignature(), otherJws.getEncodedSignature());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getEncodedHeader(), getEncodedPayload(), getEncodedSignature());
	}

	public static JwsAcmeBuilder builder() {
		return new JwsAcmeBuilder();
	}
}
