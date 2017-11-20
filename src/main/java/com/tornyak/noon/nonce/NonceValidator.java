package com.tornyak.noon.nonce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class NonceValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(NonceValidator.class);

	@Autowired
	NonceRepository nonceRepository;

	public boolean isValid(Nonce nonce) {
		LOGGER.debug("isValid: {}", nonce);
		return nonceRepository.getAndDelete(nonce.toString()).isPresent();
	}
}
