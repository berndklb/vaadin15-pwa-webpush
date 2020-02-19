package com.viessmann.vaadin15.typescript.views.masterdetail;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.viessmann.vaadin15.typescript.dto.Notification;
import com.viessmann.vaadin15.typescript.service.PushService;
import com.viessmann.vaadin15.typescript.service.SubscriptionService;


@Route(value = "Master-Detail")
@PageTitle("Master Detail")
@CssImport("views/masterdetail/master-detail-view.css")
public class MasterDetailView extends Div implements AfterNavigationObserver {

    @Autowired
    private SubscriptionService subService;
    @Autowired
    private PushService pushService;

    public MasterDetailView() {
        setId("master-detail-view");

        Button btnSendNotification = new Button("Notification without payload");
        btnSendNotification.addClickListener(e -> sendWithoutPayload());
        this.add(btnSendNotification);
        
        Button btnSend = new Button("Notification with payload");
        btnSend.addClickListener(e -> sendWithPayload());
        this.add(btnSend);
    }

    private void sendWithoutPayload() {
    	subService.getAll().forEach(e -> pushService.sendPushMessage(e, null));
	}
    
    private void sendWithPayload() {
    	String payload = "<html><i>My custom payload</i></html>";
    	Notification noti = new Notification("Test with payload");
    	noti.setBody(payload);
    	try {
			pushService.sendPushMessageToAllSubscribers(subService.getAll(), noti);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        // Lazy init of the grid items, happens only when we are sure the view will be
        // shown to the user
//        employees.setItems(service.getEmployees());
    }
}
