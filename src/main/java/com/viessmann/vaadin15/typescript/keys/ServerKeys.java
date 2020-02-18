package com.viessmann.vaadin15.typescript.keys;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerKeys {
	public final static Logger logger = LoggerFactory.getLogger(ServerKeys.class);

	@Value("${pwa.publickey.path}")
	private String publicKeyPath;

	@Value("${pwa.privatekey.path}")
	private String privateKeyPath;

	private ECPublicKey publicKey;

	private byte[] publicKeyUncompressed;

	private String publicKeyBase64;

	private ECPrivateKey privateKey;

	private final CryptoService cryptoService;

	public ServerKeys(CryptoService cryptoService) {
		this.cryptoService = cryptoService;
	}

	public byte[] getPublicKeyUncompressed() {
		return this.publicKeyUncompressed;
	}

	public String getPublicKeyBase64() {
		return this.publicKeyBase64;
	}

	public ECPrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public ECPublicKey getPublicKey() {
		return this.publicKey;
	}

	@PostConstruct
	private void initKeys() {
		Path appServerPublicKeyFile = Paths.get(this.publicKeyPath);
		Path appServerPrivateKeyFile = Paths.get(this.privateKeyPath);

		if (Files.exists(appServerPublicKeyFile) && Files.exists(appServerPrivateKeyFile)) {
			try {
				byte[] appServerPublicKey = Files.readAllBytes(appServerPublicKeyFile);
				byte[] appServerPrivateKey = Files.readAllBytes(appServerPrivateKeyFile);

				this.publicKey = (ECPublicKey) this.cryptoService.convertX509ToECPublicKey(appServerPublicKey);
				this.privateKey = (ECPrivateKey) this.cryptoService.convertPKCS8ToECPrivateKey(appServerPrivateKey);

				this.publicKeyUncompressed = CryptoService.toUncompressedECPublicKey(this.publicKey);

				this.publicKeyBase64 = Base64.getUrlEncoder().withoutPadding()
						.encodeToString(this.publicKeyUncompressed);
			} catch (IOException | InvalidKeySpecException e) {
				logger.error("read files", e);
			}
		} else {
			try {
				KeyPair pair = this.cryptoService.getKeyPairGenerator().generateKeyPair();
				this.publicKey = (ECPublicKey) pair.getPublic();
				this.privateKey = (ECPrivateKey) pair.getPrivate();
				Files.write(appServerPublicKeyFile, this.publicKey.getEncoded());
				Files.write(appServerPrivateKeyFile, this.privateKey.getEncoded());

				this.publicKeyUncompressed = CryptoService.toUncompressedECPublicKey(this.publicKey);

				this.publicKeyBase64 = Base64.getUrlEncoder().withoutPadding()
						.encodeToString(this.publicKeyUncompressed);
			} catch (IOException e) {
				logger.error("write files", e);
			}
		}

	}

}
