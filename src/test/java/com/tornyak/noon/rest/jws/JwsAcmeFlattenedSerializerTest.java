package com.tornyak.noon.rest.jws;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import org.jose4j.jwk.JsonWebKey.Factory;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tornyak.noon.nonce.Nonce;
import com.tornyak.noon.rest.jws.JwsAcmeHeader.JwsAcmeHeaderBuilder;
import com.tornyak.noon.rest.payload.NewAccountReq;

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
		JwsAcmeHeader jwsAcmeHeader = defaultHeaderBuilder().jwk(Factory.newJwk(keyPair.getPublic())).build();
		JwsAcme jwsAcme = JwsAcme.builder().header(jwsAcmeHeader).payload(payloadBytes()).key(keyPair.getPrivate())
				.signed();
		serializeDeserialize(jwsAcme);
	}

	@Test
	public void testSerializeDeserializeKid() throws Exception {

		JwsAcmeHeader jwsAcmeHeader = defaultHeaderBuilder()
				.kid(URI.create("http://www.tornyak.com/acme/acct/1").toURL()).build();
		JwsAcme jwsAcme = JwsAcme.builder().header(jwsAcmeHeader).payload(payloadBytes()).key(keyPair.getPrivate())
				.signed();
		serializeDeserialize(jwsAcme);
	}

	private void serializeDeserialize(JwsAcme jwsAcme) throws Exception {
		// TODO Auto-generated method stub
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
