package com.tornyak.noon;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import org.jose4j.jwk.JsonWebKey.Factory;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tornyak.noon.model.Directory;
import com.tornyak.noon.nonce.Nonce;
import com.tornyak.noon.rest.jws.JwsAcme;
import com.tornyak.noon.rest.jws.JwsAcmeFlattenedSerializer;
import com.tornyak.noon.rest.jws.JwsAcmeHeader;
import com.tornyak.noon.rest.payload.NewAccountReq;
import com.tornyak.noon.rest.payload.NewAccountRsp;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class NoonApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private Directory directory;

	private static KeyPair keyPair;
	private static ObjectMapper jsonObjectMapper;

	@BeforeClass
	public static void setUp() throws Exception {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(2048, SecureRandom.getInstance("NativePRNGNonBlocking"));
		keyPair = keyGen.generateKeyPair();
		jsonObjectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(JwsAcme.class, new JwsAcmeFlattenedSerializer());
		jsonObjectMapper.registerModule(module);
	}

	@Test
	public void getDirectory() throws Exception {
		ResponseEntity<Directory> response = restTemplate.getForEntity("/acme", Directory.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		assertThat(response.getBody()).isEqualTo(directory);
	}

	@Test
	public void getNonce() throws Exception {
		HttpHeaders headers = restTemplate.headForHeaders("/acme/new-nonce");
		assertThat(headers.get("Replay-Nonce")).isNotEmpty();
		assertThat(headers.getCacheControl()).isEqualTo("no-store");
	}

	@Test
	public void createAccount() throws Exception {

		JwsAcmeHeader jwsAcmeHeader = JwsAcmeHeader.builder().algorithm(AlgorithmIdentifiers.RSA_USING_SHA256)
				.jwk(Factory.newJwk(keyPair.getPublic())).nonce(new Nonce("3YVNunvM6OSd9JQNY_6QvA"))
				.url(URI.create("http://www.tornyak.com/acme/new-account").toURL()).build();

		JwsAcme jwsAcme = JwsAcme.builder().header(jwsAcmeHeader).payload(newAccountReq()).key(keyPair.getPrivate())
				.signed();

		ResponseEntity<NewAccountRsp> response = postJws(jwsAcme, NewAccountRsp.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
	}

	private byte[] newAccountReq() throws Exception {
		NewAccountReq account = new NewAccountReq(
				new String[] { "mailto:cert-admin@example.com", "tel:+12025551212", "http://www.ninja.jp" }, true);

		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			jsonObjectMapper.writeValue(bos, account);
			return bos.toByteArray();
		}
	}

	private <T> ResponseEntity<T> postJws(JwsAcme jws, Class<T> responseType) throws JsonProcessingException {
		String serialized = jsonObjectMapper.writeValueAsString(jws);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> httpEntity = new HttpEntity<>(serialized, httpHeaders);
		return restTemplate.postForEntity("/acme/new-account", httpEntity, responseType);
	}
}
