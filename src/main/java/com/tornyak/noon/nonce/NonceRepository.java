package com.tornyak.noon.nonce;

import java.util.Optional;

public interface NonceRepository {
	Optional<Nonce> getAndDelete(String nonceId);
}
