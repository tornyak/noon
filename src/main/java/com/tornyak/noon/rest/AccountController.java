package com.tornyak.noon.rest;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.function.Supplier;

import org.jose4j.lang.JoseException;
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
import com.tornyak.noon.nonce.Nonce;
import com.tornyak.noon.nonce.NonceRepository;
import com.tornyak.noon.rest.error.ProblemDetails;
import com.tornyak.noon.rest.jws.JwsAcme;
import com.tornyak.noon.rest.payload.NewAccountReq;
import com.tornyak.noon.rest.payload.NewAccountRsp;

@RestController
@RequestMapping("/acme/new-account")
public class AccountController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private Supplier<Nonce> nonceGenerator;

	@Autowired
	private NonceRepository nonceRepository;

	@Autowired
	private NoonConfig config;

	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping
	public ResponseEntity<?> newAccount(@RequestBody JwsAcme input) {
		Nonce nonce;
		try {
			LOGGER.debug("newAccount: header={}", input.getHeader());
			String msgNonce = input.getHeader().getNonce().toString();
			if (!nonceRepository.getAndDelete(msgNonce).isPresent()) {
				nonce = nonceGenerator.get();
				return ResponseEntity.badRequest().header("Replay-Nonce", nonce.toString())
						.header("Content-Type", "application/problem+json").header("Content-Language", "en")
						.body(new ProblemDetails("urn:ietf:params:acme:error:unauthorized",
								"Unknown nonce: " + msgNonce, "https://acme.com/doc/unauthorized"));
			}
		} catch (MalformedURLException | JoseException e) {
			nonce = nonceGenerator.get();
			return ResponseEntity.badRequest().header("Replay-Nonce", nonce.toString())
					.header("Content-Type", "application/problem+json").header("Content-Language", "en")
					.body(new ProblemDetails("urn:ietf:params:acme:error:unauthorized", "Malformed nonce",
							"https://acme.com/doc/unauthorized"));
		}

		try {
			input.setKey(input.getHeader().getJwk().getKey());
			NewAccountReq req = objectMapper.readValue(input.getPayloadBytes(), NewAccountReq.class);

			nonce = nonceGenerator.get();
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
