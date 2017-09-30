package com.tornyak.noon.rest;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/acme/new-nonce")
public class NonceController {
	
	@RequestMapping(method = RequestMethod.HEAD)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public ResponseEntity<String> newNonce() {
		 return ResponseEntity.noContent().header("Replay-Nonce", "oFvnlFP1wIhRlYS2jTaXbA").cacheControl(CacheControl.noStore()).build();
	}
	
}
