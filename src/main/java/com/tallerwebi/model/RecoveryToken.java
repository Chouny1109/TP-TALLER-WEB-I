package com.tallerwebi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Date;
@Entity
public class RecoveryToken {

    @Id
    private String token;

    @OneToOne
    private Usuario usuario;

    private LocalDateTime fechaCreacion;

    private LocalDateTime expiracion;

    private Boolean usado = false;



    public RecoveryToken() {}

    public RecoveryToken(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
        this.fechaCreacion = LocalDateTime.now();
        this.expiracion = fechaCreacion.plusHours(12); // ejemplo
    }
    public void setUsado(Boolean usado) {
        this.usado = usado;
    }
    public Boolean getUsado() {
        return usado;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public LocalDateTime getExpiracion() {
        return expiracion;
    }
    public void setExpiracion(LocalDateTime expiracion) {
        this.expiracion = expiracion;
    }

}

