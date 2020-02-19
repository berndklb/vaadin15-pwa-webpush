package com.viessmann.vaadin15.typescript.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.viessmann.vaadin15.typescript.dto.Subscription;

@Service
public class SubscriptionService {

	private final Map<String, Subscription> subscriptions = new ConcurrentHashMap<>();
	
	public void addSubscription(Subscription subscription) {
		System.out.println("Subscription received on server: " + subscription);
		this.subscriptions.put(subscription.getEndpoint(), subscription);
	}
	
	public List<Subscription> getAll() {
		return new ArrayList<>(subscriptions.values());
	}
}
