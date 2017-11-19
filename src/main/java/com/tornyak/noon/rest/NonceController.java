package com.tornyak.noon.rest;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tornyak.noon.nonce.Nonce;

@RestController
@RequestMapping("/acme/new-nonce")
public class NonceController {
	private static final Logger LOGGER = LoggerFactory.getLogger(NonceController.class);

	@Autowired
	Supplier<Nonce> nonceGenerator;

	@RequestMapping(method = RequestMethod.HEAD)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public ResponseEntity<String> newNonce() {
		Nonce nonce = nonceGenerator.get();
		LOGGER.debug("newNonce: {}", nonce);
		return ResponseEntity.noContent().header("Replay-Nonce", nonce.toString()).cacheControl(CacheControl.noStore())
				.build();
	}

}
