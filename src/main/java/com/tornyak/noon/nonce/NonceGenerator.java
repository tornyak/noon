package com.tornyak.noon.nonce;

import java.security.SecureRandom;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NonceGenerator implements Supplier<Nonce> {
	private static final Logger LOGGER = LoggerFactory.getLogger(NonceGenerator.class);
	
	private SecureRandom secureRandom;
	private int nonceSize;
	
	public NonceGenerator(SecureRandom secureRandom, int nonceSize) {
		this.secureRandom = secureRandom;
		this.secureRandom.setSeed(System.nanoTime());
		this.nonceSize = nonceSize;
	}

	@Override
	public Nonce get() {
		byte[] randomBytes = new byte[nonceSize];
		secureRandom.nextBytes(randomBytes);
		return new Nonce(randomBytes);
	}
}
