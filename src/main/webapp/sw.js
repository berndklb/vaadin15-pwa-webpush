self.addEventListener('activate', event => event.waitUntil(clients.claim()));

self.addEventListener('push', event => event.waitUntil(handlePushEvent(event)));

async function handlePushEvent(event) {
	console.info('push event emitted');
	
	console.log(this.swRegistration);
	console.log("self:");
	console.log(self);
	console.log(self.registration);
	self.registration.showNotification('Numbers API', {
        body: 'A new fact has arrived',
        tag: 'numberfact',
//        icon: 'numbers.png'
      });
}