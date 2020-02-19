package com.viessmann.vaadin15.typescript.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.viessmann.vaadin15.typescript.dto.Subscription;
import com.viessmann.vaadin15.typescript.service.SubscriptionService;

@RestController
public class SubscriptionController {

	private final SubscriptionService subService;
	
	public SubscriptionController(SubscriptionService subService) {
		this.subService = subService;
	}
	
	@PostMapping("/subscribe")
	@ResponseStatus(HttpStatus.CREATED)
	public void subscribe(@RequestBody Subscription subscription) {
		this.subService.addSubscription(subscription);
	}
}
