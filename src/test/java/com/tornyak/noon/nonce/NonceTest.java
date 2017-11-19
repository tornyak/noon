package com.tornyak.noon.nonce;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;

import org.junit.Test;

public class NonceTest {

	@Test(expected = IllegalArgumentException.class)
	public void nonceFromIllegalBase64UrlString() {
		Nonce.fromBase64UrlString("{}nonceString![][|§<>");
	}

	@Test
	public void nonceFromBase64UrlString() {
		String nonceString = "Ok10CC_9fwQ-r55v44-YMQ";
		Nonce nonce = Nonce.fromBase64UrlString(nonceString);
		assertThat(nonce.toString()).isEqualTo(nonceString);
		assertThat(nonce.getBytes()).isEqualTo(nonceString.getBytes());
	}
	
	@Test
	public void nonceFromPaddedBase64UrlString() {
		String paddedString = "Ok10CC_9fwQ-r55v44-YMQ==";
		String nonceString = "Ok10CC_9fwQ-r55v44-YMQ";
		Nonce nonce = Nonce.fromBase64UrlString(paddedString);
		assertThat(nonce.toString()).isEqualTo(nonceString);
		assertThat(nonce.getBytes()).isEqualTo(nonceString.getBytes());
	}
	
	
	
	@Test
	public void nonceFromRandomString() {
		String nonceString = "{}nonceString![][|§<>";
		Nonce nonce = Nonce.fromString(nonceString);
		Nonce anotherNonce = Nonce.fromBase64UrlString(nonce.toString());
		assertThat(nonce).isEqualTo(anotherNonce);
	}

	@Test
	public void nonceFromBytes() {
		byte[] bytes = new byte[] { 3, (byte) 236, (byte) 255, (byte) 224, (byte) 193 };
		Nonce nonce = Nonce.fromBytes(bytes);
		String nonceSent = nonce.toString();
		Nonce nonceReceived = Nonce.fromBase64UrlString(nonceSent);
		assertThat(nonce.toString()).isEqualTo(nonceReceived.toString());
		assertThat(nonce.getBytes()).isEqualTo(nonceReceived.getBytes());
	}
	
	@Test
	public void nonceHasNoPadding() {
		String randomString = "{}nonceString![][|§<>";
		Nonce nonce = Nonce.fromString(randomString);
		String base64Encoded = Base64.getUrlEncoder().encodeToString(randomString.getBytes());
		assertThat(base64Encoded).endsWith("=");
		assertThat(nonce.toString()).doesNotEndWith("=");
	}
}
