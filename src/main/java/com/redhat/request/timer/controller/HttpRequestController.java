package com.redhat.request.timer.controller;

import io.swagger.annotations.ApiOperation;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("request-timer/v1/api")
public class HttpRequestController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@CrossOrigin
	@ApiOperation("Test url")
	@RequestMapping(value = "testUrl", method = RequestMethod.GET)
	public ResponseEntity<String> testUrl(
			@RequestParam(required = true) String url,
			@RequestParam(required = false, name = "proxyHost") String proxyHost,
			@RequestParam(required = false, name = "proxyPort") Integer proxyPort) {

		log.debug("url: {}", url);

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

		if (proxyHost != null && !proxyHost.isEmpty() && proxyPort != null) {
			log.info("Proxy {}:{} is active.", proxyHost, proxyPort);
			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost,
					proxyPort));
			requestFactory.setProxy(proxy);
		}

		RestTemplate rt = new RestTemplate(requestFactory);
		ResponseEntity<String> response = rt.getForEntity(url, String.class);

		return response;
	}

}
