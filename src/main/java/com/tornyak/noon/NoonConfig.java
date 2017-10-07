package com.tornyak.noon;

import java.net.URL;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="noon.acme")
public class NoonConfig {
	private URL basepath;

	public URL getBasepath() {
		return basepath;
	}

	public void setBasepath(URL acmeBasepath) {
		this.basepath = acmeBasepath;
	}
}
