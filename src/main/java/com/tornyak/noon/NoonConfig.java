package com.tornyak.noon;

import java.net.URL;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "noon.acme")
public class NoonConfig {
	public static final int MIN_NONCE_SIZE = 6;
	public static final int MAX_NONCE_SIZE = 32;

	private URL basepath;
	private int nonceSize;

	public URL getBasepath() {
		return basepath;
	}

	public void setBasepath(URL acmeBasepath) {
		this.basepath = acmeBasepath;
	}

	public int getNonceSize() {
		return nonceSize;
	}

	public void setNonceSize(int nonceSize) {
		if (nonceSize < MIN_NONCE_SIZE || nonceSize > MAX_NONCE_SIZE) {
			throw new IllegalArgumentException(String.format("nonceSize %d not in the allowed range [%d, %d]",
					nonceSize, MIN_NONCE_SIZE, MAX_NONCE_SIZE));
		}
		this.nonceSize = nonceSize;
	}
}
