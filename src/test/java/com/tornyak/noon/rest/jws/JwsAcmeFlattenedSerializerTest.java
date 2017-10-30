package com.tornyak.noon.rest.jws;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKey.Factory;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tornyak.noon.model.NewAccountReq;
import com.tornyak.noon.nonce.Nonce;
import com.tornyak.noon.rest.jws.JwsAcmeHeader.JwsAcmeHeaderBuilder;

public class JwsAcmeFlattenedSerializerTest {

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
	public void testSerializeDeserializeJwk() throws Exception {

		JwsAcmeHeader jwsAcmeHeader = JwsAcmeHeader.builder().algorithm(AlgorithmIdentifiers.RSA_USING_SHA256)
				.jwk(Factory.newJwk(keyPair.getPublic())).nonce(new Nonce("3YVNunvM6OSd9JQNY_6QvA"))
				.url(URI.create("http://www.tornyak.com/acme/new-account").toURL()).build();

		JwsAcme jwsAcme = new JwsAcme();
		jwsAcme.setHeader(jwsAcmeHeader);
		jwsAcme.setPayload(payloadBytes());
		jwsAcme.setKey(keyPair.getPrivate());
		jwsAcme.sign();

		String serialized = jsonObjectMapper.writeValueAsString(jwsAcme);

		JwsAcme jwsAcmeDeserialized = jsonObjectMapper.readValue(serialized, JwsAcme.class);
		JsonWebKey jwk = jwsAcmeDeserialized.getHeader().getJwk();
		jwsAcmeDeserialized.setKey(jwk.getKey());

		assertThat(jwsAcmeDeserialized.isSignatureValid()).isTrue();
		assertEquals(jwsAcme, jwsAcmeDeserialized);
	}

	@Test
	public void testSerializeDeserializeKid() throws Exception {

		JwsAcmeHeader jwsAcmeHeader = defaultHeaderBuilder()
				.kid(URI.create("http://www.tornyak.com/acme/acct/1").toURL()).build();

		JwsAcme jwsAcme = new JwsAcme();
		jwsAcme.setHeader(jwsAcmeHeader);
		jwsAcme.setPayload(payloadBytes());
		jwsAcme.setKey(keyPair.getPrivate());
		jwsAcme.sign();

		String serialized = jsonObjectMapper.writeValueAsString(jwsAcme);

		JwsAcme jwsAcmeDeserialized = jsonObjectMapper.readValue(serialized, JwsAcme.class);
		jwsAcmeDeserialized.setKey(keyPair.getPublic());

		assertThat(jwsAcmeDeserialized.isSignatureValid()).isTrue();
		assertEquals(jwsAcme, jwsAcmeDeserialized);
	}

	private JwsAcmeHeaderBuilder defaultHeaderBuilder() throws MalformedURLException {
		return JwsAcmeHeader.builder().algorithm(AlgorithmIdentifiers.RSA_USING_SHA256)
				.nonce(new Nonce("3YVNunvM6OSd9JQNY_6QvA"))
				.url(URI.create("http://www.tornyak.com/acme/new-account").toURL());
	}

	private byte[] payloadBytes() throws Exception {
		NewAccountReq account = new NewAccountReq(
				new String[] { "mailto:cert-admin@example.com", "tel:+12025551212", "http://www.ninja.jp" }, true);

		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			jsonObjectMapper.writeValue(bos, account);
			return bos.toByteArray();
		}
	}
}
