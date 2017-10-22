package com.tornyak.noon.nonce;

import java.security.SecureRandom;
import java.util.function.Supplier;

import org.springframework.cache.annotation.CachePut;

public class NonceGenerator implements Supplier<Nonce> {

	private SecureRandom secureRandom;
	private int nonceSize;

	public NonceGenerator(SecureRandom secureRandom, int nonceSize) {
		this.secureRandom = secureRandom;
		this.secureRandom.setSeed(System.nanoTime());
		this.nonceSize = nonceSize;
	}

	@Override
	@CachePut(value = "nonce", key = "#result.toString()")
	public Nonce get() {
		byte[] randomBytes = new byte[nonceSize];
		secureRandom.nextBytes(randomBytes);
		return new Nonce(randomBytes);
	}
}
