package com.tornyak.noon.rest.jws.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tornyak.noon.NoonConfig;
import com.tornyak.noon.nonce.Nonce;
import com.tornyak.noon.nonce.NonceRepository;

public class NonceValidator implements Validator {
	@Autowired
	private NoonConfig config;

	@Autowired
	private NonceRepository nonceRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return Nonce.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Nonce nonce = (Nonce) target;
		if (nonce.getBytes().length != config.getNonceSize()) {
			errors.reject("nonce.size");
		}
		Optional<Nonce> optNonce = nonceRepository.getAndDelete(nonce.toString());
		if (!optNonce.isPresent()) {
			errors.reject("nonce.value");
		}
	}
}