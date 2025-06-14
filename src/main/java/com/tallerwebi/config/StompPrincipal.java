package com.tallerwebi.config;


import java.security.Principal;

public class StompPrincipal implements Principal {
    private String name;

    public StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Principal)) return false;
        Principal that = (Principal) o;
        return this.name.equals(that.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
