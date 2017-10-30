package com.tornyak.noon.rest.jws;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JwsAcmeFlattenedSerializer extends StdSerializer<JwsAcme> {

	private static final long serialVersionUID = -6733897070293143114L;

	public JwsAcmeFlattenedSerializer() {
		this(null);
	}

	public JwsAcmeFlattenedSerializer(Class<JwsAcme> t) {
		super(t);
	}

	@Override
	public void serialize(JwsAcme value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("protected", value.getEncodedHeader());
		gen.writeStringField("payload", value.getEncodedPayload());
		gen.writeStringField("signature", value.getEncodedSignature());
		gen.writeEndObject();
	}

}
