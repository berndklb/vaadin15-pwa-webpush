package com.viessmann.vaadin15.typescript.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viessmann.vaadin15.typescript.dto.Subscription;
import com.viessmann.vaadin15.typescript.keys.CryptoService;
import com.viessmann.vaadin15.typescript.keys.ServerKeys;

@Service
public class PushService {

	final static Logger logger = LoggerFactory.getLogger(PushService.class);
	
	private final ServerKeys serverKeys;
	private final Algorithm jwtAlgorithm;
	private final HttpClient httpClient;
	private final CryptoService cryptoService;
	private final ObjectMapper objectMapper;
	
	  public PushService(ServerKeys serverKeys, CryptoService cryptoService,
		      ObjectMapper objectMapper) {
		    this.serverKeys = serverKeys;
		    this.cryptoService = cryptoService;
		    this.httpClient = HttpClient.newHttpClient();
		    this.objectMapper = objectMapper;

		    this.jwtAlgorithm = Algorithm.ECDSA256(this.serverKeys.getPublicKey(),
		        this.serverKeys.getPrivateKey());
		  }
	
	public boolean sendPushMessage(Subscription subscription, byte[] body) {
	    String origin = null;
	    try {
	      URL url = new URL(subscription.getEndpoint());
	      origin = url.getProtocol() + "://" + url.getHost();
	    }
	    catch (MalformedURLException e) {
	      logger.error("create origin", e);
	      return true;
	    }

	    Date today = new Date();
	    Date expires = new Date(today.getTime() + 12 * 60 * 60 * 1000);

	    String token = JWT.create().withAudience(origin).withExpiresAt(expires)
	        .withSubject("mailto:example@example.com").sign(this.jwtAlgorithm);

	    URI endpointURI = URI.create(subscription.getEndpoint());

	    Builder httpRequestBuilder = HttpRequest.newBuilder();
	    if (body != null) {
	      httpRequestBuilder.POST(BodyPublishers.ofByteArray(body))
	          .header("Content-Type", "application/octet-stream")
	          .header("Content-Encoding", "aes128gcm");
	    }
	    else {
	      httpRequestBuilder.POST(BodyPublishers.noBody());
	    }

	    HttpRequest request = httpRequestBuilder.uri(endpointURI).header("TTL", "180")
	        .header("Authorization",
	            "vapid t=" + token + ", k=" + this.serverKeys.getPublicKeyBase64())
	        .build();
	    try {
	      HttpResponse<Void> response = this.httpClient.send(request,
	          BodyHandlers.discarding());

	      switch (response.statusCode()) {
	      case 201:
	        logger.info("Push message successfully sent: {}",
	            subscription.getEndpoint());
	        break;
	      case 404:
	      case 410:
	        logger.warn("Subscription not found or gone: {}",
	            subscription.getEndpoint());
	        // remove subscription from our collection of subscriptions
	        return true;
	      case 429:
	        logger.error("Too many requests: {}", request);
	        break;
	      case 400:
	        logger.error("Invalid request: {}", request);
	        break;
	      case 413:
	        logger.error("Payload size too large: {}", request);
	        break;
	      default:
	        logger.error("Unhandled status code: {} / {}", response.statusCode(),
	            request);
	      }
	    }
	    catch (IOException | InterruptedException e) {
	      logger.error("send push message", e);
	    }

	    return false;
	  }
}
