if ('serviceWorker' in navigator) {
	console.log(navigator.serviceWorker);
	
	init();
	
}

async function init() {
	navigator.serviceWorker.register("/sw.js", {
	    scope: "/"
	  }).then(function(registration) {
	  });
	
	
	fetch('/publicSigningKey')
    .then(response => response.arrayBuffer())
    .then(key => initPushRights(key))
    .finally(() => console.info('Application Server Public Key fetched from the server'));
	
}

async function initPushRights(key) {
	this.publicSigningKey = key;
	
	await navigator.serviceWorker.ready;
	console.info('Service Worker has been installed and is ready');
	console.log(this.publicSigningKey);
	
	const registration = await navigator.serviceWorker.ready;
	const subscription = await registration.pushManager.subscribe({
	    userVisibleOnly: true,
	    applicationServerKey: this.publicSigningKey
	});
	
	console.info(`Subscribed to Push Service: ${subscription.endpoint}`);
	
	await fetch("/subscribe", {
	    method: 'POST',
	    body: JSON.stringify(subscription),
	    headers: {
	      "content-type": "application/json"
	    }
	  });

	console.info('Subscription info sent to the server');
}

