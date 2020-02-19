self.addEventListener('activate', event => event.waitUntil(clients.claim()));

self.addEventListener('push', event => event.waitUntil(handlePushEvent(event)));

async function handlePushEvent(event) {
	console.info('push event emitted');

	console.log(event.data);
	
	if(event.data) {
		const msg = event.data.json();
		console.log(msg);
		// notification with given payload
		self.registration.showNotification(msg.title, {
			body: msg.body,
			tag: 'test',
//        icon: 'numbers.png'
		});
	}else {
		self.registration.showNotification('Numbers API', {
			body: 'A new fact has arrived',
			tag: 'numberfact',
//        icon: 'numbers.png'
		});
	}
}