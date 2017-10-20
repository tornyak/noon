package com.tornyak.noon.nonce;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.SecureRandom;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class NonceGeneratorTest {

	static final int NONCE_SIZE_BYTES = 16;
	SecureRandom secureRng = new SecureRandom();
	NonceGenerator nonceGenerator = new NonceGenerator(secureRng, NONCE_SIZE_BYTES);

	@Test
	public void generateNonce() {
		Nonce nonce = nonceGenerator.get();
		assertThat(nonce).isNotNull();
		assertThat(nonce.getBytes().length).isEqualTo(NONCE_SIZE_BYTES);
	}

	@Test
	public void nonceUnique() {
		int expectedSize = 100;
		Set<Nonce> nonces = Stream.
				generate(nonceGenerator).
				limit(expectedSize).
				collect(Collectors.toSet());

		assertThat(nonces.size()).isEqualTo(expectedSize);
		nonces.forEach(nonce -> {
			assertThat(nonce).isNotNull();
			assertThat(nonce.getBytes().length).isEqualTo(NONCE_SIZE_BYTES);
		});
	}
	
	@Test
	public void encodeBase64Url() {
		int expectedSize = 100;
		Set<Nonce> nonces = Stream.
				generate(nonceGenerator).
				limit(expectedSize).
				collect(Collectors.toSet());

		assertThat(nonces.size()).isEqualTo(expectedSize);
		nonces.forEach(nonce -> {
			assertThat(nonce.toBase64UrlString()).doesNotEndWith("=");
		});
	}
}
