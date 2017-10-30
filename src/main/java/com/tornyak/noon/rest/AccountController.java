package com.tornyak.noon.rest;

import java.net.URI;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tornyak.noon.NoonConfig;
import com.tornyak.noon.model.Account.Status;
import com.tornyak.noon.model.Directory;
import com.tornyak.noon.model.NewAccountReq;
import com.tornyak.noon.model.NewAccountRsp;
import com.tornyak.noon.nonce.Nonce;
import com.tornyak.noon.rest.jws.JwsAcme;

@RestController
@RequestMapping("/acme/new-account")
public class AccountController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Directory.class);

	@Autowired
	private Supplier<Nonce> nonceGenerator;

	@Autowired
	private NoonConfig config;

	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping
	public ResponseEntity<NewAccountRsp> newAccount(@RequestBody JwsAcme input) {
		LOGGER.debug("newAccount: input: {}", input);
		try {
			input.setKey(input.getHeader().getJwk().getKey());
			NewAccountReq req = objectMapper.readValue(input.getPayloadBytes(), NewAccountReq.class);

			Nonce nonce = nonceGenerator.get();
			LOGGER.debug("nonce: {}", nonce);
			return ResponseEntity.created(URI.create(config.getBasepath() + "/acct/1"))
					.header("Replay-Nonce", nonce.toString()).contentType(MediaType.APPLICATION_JSON_UTF8)
					.body(new NewAccountRsp(Status.DEACTIVATED, req.getContact()));
		} catch (Exception e) {
			LOGGER.error("newAccount failed:", e);
			return ResponseEntity.badRequest().build();
		}
	}

}
