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

    @Column(unique = true, nullable = false)
    private String nombreUsuario;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // o EnumType.ORDINAL, pero mejor STRING
    @Column(name = "rol")
    private ROL_USUARIO rol;

    private Boolean activo = false;

    @Column(unique = true)
    private String token;

    private Integer nivel = 0;

    @ManyToMany
    private List<Partida> partida;

    /*
        Fetch Lazy -> No te trae todos los datos de ese usuario hasta que llames explicitamente a ese metodo.
        Fetch Eager -> Te tree todos los datos de ese usuario (Si tenemos muchos datos puede ser ineficiente).
     */
    @OneToMany(mappedBy = "usuario",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<UsuarioMision> misiones;

    public Usuario(String nombreUsuario, String email, String password) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.misiones = new ArrayList<>();
    }

    public Usuario() {
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

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

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public List<Partida> getPartida() {
        return partida;
    }

    public void setPartida(List<Partida> partida) {
        this.partida = partida;
    }

    public List<UsuarioMision> getMisiones() {
        return misiones;
    }

    public void setMisiones(List<UsuarioMision> misiones) {
        this.misiones = misiones;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}