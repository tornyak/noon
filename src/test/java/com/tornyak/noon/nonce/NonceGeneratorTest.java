package com.tornyak.noon.nonce;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = NonceTestConfig.class)
public class NonceGeneratorTest {

	@Autowired
	Supplier<Nonce> nonceGenerator;

	@Test
	public void generateNonce() {
		Nonce nonce = nonceGenerator.get();
		assertThat(nonce).isNotNull();
		assertThat(nonce.getBytes().length).isEqualTo(NonceTestConfig.NONCE_SIZE_BYTES);
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
			assertThat(nonce.getBytes().length).isEqualTo(NonceTestConfig.NONCE_SIZE_BYTES);
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
			assertThat(nonce.toString()).doesNotEndWith("=");
		});
	}
	
	@Test
	public void base64UrlEncodingAccordingToRFC7515() {
		byte[] nonceBytes = new byte[]{3, (byte) 236, (byte) 255, (byte) 224, (byte) 193};
		Nonce nonce = new Nonce(nonceBytes);
		assertThat(nonce.toString()).isEqualTo("A-z_4ME");
	}
}
