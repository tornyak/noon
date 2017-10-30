package com.tornyak.noon.rest.jws;

import java.net.URL;

import org.jose4j.jwk.JsonWebKey;

import com.tornyak.noon.nonce.Nonce;

public final class JwsAcmeHeader {

	private final String algorithm;
	private final Nonce nonce;
	private final URL keyId;
	private final JsonWebKey jwk;
	private final URL url;

	public static class JwsAcmeHeaderBuilder {
		private String algorithm;
		private Nonce nonce;
		private URL keyId;
		private JsonWebKey jwk;
		private URL url;

		public JwsAcmeHeaderBuilder algorithm(String algorithm) {
			this.algorithm = algorithm;
			return this;
		}

		public JwsAcmeHeaderBuilder nonce(Nonce nonce) {
			this.nonce = nonce;
			return this;
		}
		
		public JwsAcmeHeaderBuilder kid(URL keyId) {
			this.keyId = keyId;
			return this;
		}

		public JwsAcmeHeaderBuilder jwk(JsonWebKey jwk) {
			this.jwk = jwk;
			return this;
		}

		public JwsAcmeHeaderBuilder url(URL url) {
			this.url = url;
			return this;
		}

		public JwsAcmeHeader build() {
			assertNotNull(algorithm, "algorithm");
			assertNotNull(nonce, "nonce");
			assertNotNull(url, "url");
			if(keyId == null && jwk == null) {
				throw new IllegalStateException("One of \"keyId\" \"jwk\" must be set ");
			}
			if(keyId != null && jwk != null) {
				throw new IllegalStateException("\"keyId\" \"jwk\" must not be set at the same time ");
			}
			return new JwsAcmeHeader(algorithm, nonce, keyId, jwk, url);
		}
		
		private void assertNotNull(Object o, String name) {
			if(o == null) {
				throw new IllegalStateException(String.format("%s is not set", name));
			}
		}
	}

	protected JwsAcmeHeader(String algorithm, Nonce nonce, URL keyId, JsonWebKey jwk, URL url) {
		this.algorithm = algorithm;
		this.nonce = nonce;
		this.keyId = keyId;
		this.jwk = jwk;
		this.url = url;
	}

	public static JwsAcmeHeaderBuilder builder() {
		return new JwsAcmeHeaderBuilder();
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public Nonce getNonce() {
		return nonce;
	}

	public URL getKeyId() {
		return keyId;
	}

	public JsonWebKey getJwk() {
		return jwk;
	}

	public URL getUrl() {
		return url;
	}
}
