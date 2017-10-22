package com.tornyak.noon.nonce;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.function.Supplier;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class NonceTestConfig {
	public static final int NONCE_SIZE_BYTES = 16;
	
	@Bean
	public Supplier<Nonce> nonceGenetator() {
		SecureRandom secureRng = new SecureRandom();
		return new NonceGenerator(secureRng, NONCE_SIZE_BYTES);
	}
	
	@Bean
	public NonceRepository nonceRepository() {
		return new SimpleCachedNonceRepository();
	}
	
	@Bean
    public CacheManager cacheManager() {
        // configure and return an implementation of Spring's CacheManager SPI
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("nonce")));
        return cacheManager;
    }
}
