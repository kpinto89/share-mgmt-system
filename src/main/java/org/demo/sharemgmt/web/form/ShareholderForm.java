package org.demo.sharemgmt.web.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ShareholderForm {

    @NotBlank(message = "Shareholder name is required.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Enter a valid email address.")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
