package com.tornyak.noon.rest.jws;

import java.io.IOException;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwx.CompactSerializer;
import org.jose4j.lang.JoseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Since ACME supports both flattened and common JWS formats a custom JSON
 * deserializer is created that deserializes both formats into the same
 * JwsCommonDeserializer
 * 
 * @author vanja
 *
 */
public class JwsAcmeDeserializer extends StdDeserializer<JwsAcme> {

	private static final long serialVersionUID = 5471472324563289241L;

	public JwsAcmeDeserializer() {
		this(null);
	}

	public JwsAcmeDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public JwsAcme deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

		JsonNode root = p.getCodec().readTree(p);
		assertNodeNotNull(p, root, "Failed reading JSON tree");

		String payload = getNode(p, root, "payload");

		root = switchRootIfCompleteJwsFormat(p, root);
		String signature = getNode(p, root, "signature");
		String protectedHeader = getNode(p, root, "protected");

		return buildJwsCommon(protectedHeader, payload, signature);
	}

	private String getNode(JsonParser p, JsonNode root, String nodeName) throws JsonParseException {
		JsonNode node = root.get(nodeName);
		assertNodeNotNull(p, node, String.format("\"%s\" missing", nodeName));
		return node.asText();
	}

	private void assertNodeNotNull(JsonParser p, JsonNode jsonNode, String message) throws JsonParseException {
		if (jsonNode == null) {
			throw new JsonParseException(p, message);
		}
	}

	private JsonNode switchRootIfCompleteJwsFormat(JsonParser p, JsonNode root) throws JsonParseException {
		JsonNode node = root.get("signatures");
		if (node == null) {
			return root;
		}
		if (!node.isArray()) {
			throw new JsonParseException(p, "\"signatures\" must be an array");
		}
		node = node.get(0);
		assertNodeNotNull(p, node, "\"signatures\" array is empty");
		return node;
	}

	private JwsAcme buildJwsCommon(String protectedHeader, String payload, String signature) throws IOException {
		try {
			JsonWebSignature jws;
			String compactSerialization = CompactSerializer.serialize(protectedHeader, payload, signature);
			jws = (JsonWebSignature) JsonWebSignature.fromCompactSerialization(compactSerialization);
			return new JwsAcme(jws);
		} catch (JoseException e) {
			throw new IOException(e);
		}
	}

}
