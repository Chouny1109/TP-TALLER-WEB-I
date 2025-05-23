package com.tallerwebi.model;

import java.time.LocalDateTime;
import java.util.Date;

public class RecoveryToken {
    private String token;
    private String email;


    public RecoveryToken(String token, String email) {
        this.token = token;
        this.email = email;

    }

    public String getToken() {
        return token;
    }
    public String getEmail() {
        return email;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setEmail(String email) {

        this.email = email;
    }

}
