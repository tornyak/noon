package com.tornyak.noon;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.tornyak.noon.model.Directory;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
public class NoonApplicationTests {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
    private Directory directory;
	
	
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
		assertThat(headers.getCacheControl()).isEqualTo(CacheControl.noStore().toString());
	}

}
