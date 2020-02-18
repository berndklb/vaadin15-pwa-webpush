package com.viessmann.vaadin15.typescript.views.dashboard;

import com.viessmann.vaadin15.typescript.backend.Employee;
import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import com.vaadin.flow.server.connect.exception.VaadinConnectException;

/**
 * The endpoint for the client-side Form View.
 */
@Endpoint
@AnonymousAllowed
public class DashboardEndpoint {
    public void saveEmployee(Employee employee) throws VaadinConnectException {
        throw new VaadinConnectException("not implemented");
    }
}
