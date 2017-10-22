package com.tornyak.noon.nonce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Optional;
import java.util.function.Supplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = NonceTestConfig.class)
public class SimpleCachedNonceRepositoryTest {
	
	@Autowired
	Supplier<Nonce> nonceGenerator;
	
	@Autowired
	NonceRepository nonceRepository;

	@Test
	public void generatedNonceCached() {
		Nonce nonce = nonceGenerator.get();
		assertThat(nonce).isNotNull();
		Nonce nonceFromCache = nonceRepository.getAndDelete(nonce.toString()).get();
		assertEquals(nonce, nonceFromCache);
	}
	
	@Test
	public void nonceRemovedFromCacheAfterRead() {
		Nonce nonce = nonceGenerator.get();
		Optional<Nonce> nonceFromCache = nonceRepository.getAndDelete(nonce.toString());
		nonceFromCache = nonceRepository.getAndDelete(nonce.toString());
		assertThat(nonceFromCache.isPresent()).isFalse();	
	}
}
