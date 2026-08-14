package org.demo.sharemgmt.security;

public interface AuthenticationFacade {

    String getCurrentDisplayName();

    boolean isAuthenticated();
}
