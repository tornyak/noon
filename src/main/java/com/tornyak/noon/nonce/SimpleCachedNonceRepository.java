package com.tornyak.noon.nonce;

import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@CacheConfig(cacheNames={"nonce"})
public class SimpleCachedNonceRepository implements NonceRepository {

	@Override
	@Cacheable(key="#nonceId")
	@CacheEvict(key="#nonceId")
	public Optional<Nonce> getAndDelete(String nonceId) {
		return Optional.empty();
	}
}
