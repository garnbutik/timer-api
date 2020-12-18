package com.garnbutik.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Credentials {

    @NotNull
    @Size(min = 8, message = "password must be at least eight characters")
    private String password;
    @NotNull
    @Size(min = 5, message = "username must be at least five characters")
    private String username;

    public Credentials() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
