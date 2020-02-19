package com.viessmann.vaadin15.typescript.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viessmann.vaadin15.typescript.keys.ServerKeys;

@RestController
public class KeyRestController {
	
	private final ServerKeys serverKeys;
	
	public KeyRestController(ServerKeys serverKeys) {
		this.serverKeys = serverKeys;
	}

	@GetMapping(path = "/publicSigningKey", produces = "application/octet-stream")
	public byte[] publicSigningKey() {
		System.out.println("fetch publicSigningKey as byte[]");
		return this.serverKeys.getPublicKeyUncompressed();
	}

	@GetMapping(path = "/publicSigningKeyBase64")
	public String publicSigningKeyBase64() {
		System.out.println("fetch publicSigningKey as base64");
		return this.serverKeys.getPublicKeyBase64();
	}
}
