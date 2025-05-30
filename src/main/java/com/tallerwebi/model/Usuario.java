package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.ROL_USUARIO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreUsuario;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING) // o EnumType.ORDINAL, pero mejor STRING
    @Column(name = "rol")
    private ROL_USUARIO rol;
    private Boolean activo = false;
    private Integer nivel = 0;


    @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Mision> misiones;

    public Usuario(String nombreUsuario, String email, String password) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.misiones = new ArrayList<>();
    }

    public Usuario() {
    }


    public String getNombreUsuario() { return nombreUsuario;}
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario;}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public ROL_USUARIO getRol() {
        return rol;
    }
    public void setRol(ROL_USUARIO rol) {
        this.rol = rol;
    }
    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getNivel() {
        return nivel;
    }

    public boolean activo() {
        return activo;
    }

    public void activar() {
        activo = true;
    }

    @Override
    public int hashCode() {
        return email != null ? email.toLowerCase().hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return email != null && email.equalsIgnoreCase(usuario.email);
    }

    public List<Mision> getMisiones() {
        return this.misiones;
    }

    public void setMisiones(List<Mision> misiones) {
        this.misiones = misiones;
    }
}