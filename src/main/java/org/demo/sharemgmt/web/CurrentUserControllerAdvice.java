package org.demo.sharemgmt.web;

import org.demo.sharemgmt.security.AuthenticationFacade;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentUserControllerAdvice {

    private final AuthenticationFacade authenticationFacade;

    public CurrentUserControllerAdvice(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @ModelAttribute("currentUserDisplayName")
    public String currentUserDisplayName() {
        return authenticationFacade.getCurrentDisplayName();
    }

    @ModelAttribute("userAuthenticated")
    public boolean userAuthenticated() {
        return authenticationFacade.isAuthenticated();
    }
}
