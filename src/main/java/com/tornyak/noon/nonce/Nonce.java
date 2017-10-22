package com.tornyak.noon.nonce;

import java.util.Arrays;
import java.util.Base64;

import org.springframework.util.StringUtils;

public final class Nonce {
	private final byte[] nonce;
	private final String base64UrlString;

	public Nonce(byte[] randomBytes) {
		this.nonce = randomBytes;
		this.base64UrlString = base64UrlString();
	}
	
	public byte[] getBytes() {
		return Arrays.copyOf(this.nonce, nonce.length);
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(nonce);
	}
	
	@Override
	public boolean equals(Object object) {
		if(object == null) {
			return false;
		}
		
		if(!(object instanceof Nonce)) {
			return false;
		}
		
		Nonce other = (Nonce)object;
		return Arrays.equals(this.nonce, other.nonce);
	}
	
	@Override
	public String toString() {
		return base64UrlString;
	}
	
	private String base64UrlString() {
		String encoded = Base64.getUrlEncoder().encodeToString(nonce);
		return StringUtils.trimTrailingCharacter(encoded, '=');
	}
}
