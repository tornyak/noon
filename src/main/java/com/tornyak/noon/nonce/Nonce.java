package com.tornyak.noon.nonce;

import java.util.Base64;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

public final class Nonce {
	private final String base64UrlString;

	public static Nonce fromBytes(@NotNull byte[] bytes) {
		return new Nonce(bytes);
	}

	public static Nonce fromString(@NotNull String string) {
		return Nonce.fromBytes(string.getBytes());
	}

	public static Nonce fromBase64UrlString(@NotNull String base64String) {
		validateBase64Url(base64String);
		return new Nonce(StringUtils.trimTrailingCharacter(base64String, '='));
	}

	private Nonce(byte[] randomBytes) {
		this.base64UrlString = base64UrlString(randomBytes);
	}

	private Nonce(String base64UrlString) {
		this.base64UrlString = base64UrlString;
	}

	private static void validateBase64Url(String string) {
		Base64.getUrlDecoder().decode(string);
	}

	private String base64UrlString(byte[] stringBytes) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(stringBytes);
	}

	public byte[] getBytes() {
		return base64UrlString.getBytes();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(base64UrlString);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}

		if (!(object instanceof Nonce)) {
			return false;
		}

		Nonce other = (Nonce) object;
		return Objects.equals(this.base64UrlString, other.base64UrlString);
	}

	@Override
	public String toString() {
		return base64UrlString;
	}
}
