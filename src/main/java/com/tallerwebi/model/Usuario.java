package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.ROL_USUARIO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class
Usuario {
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

    private Boolean activo;

    private Integer experiencia;

    @Column(unique = true)
    private String token;

    @Column(nullable = false)
    private Integer monedas;

    private Integer copas;

    private Integer vidas;

    @ManyToMany
    @JoinTable(
            name = "amistad",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "amigo_id")
    )
    @JsonIgnoreProperties("amigos")
    private Set<Usuario> amigos;


    /*
        Fetch Lazy -> No te trae todos los datos de ese usuario hasta que llames explicitamente a ese metodo.
        Fetch Eager -> Te tree todos los datos de ese usuario (Si tenemos muchos datos puede ser ineficiente).
     */
    @OneToMany(mappedBy = "usuario",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<UsuarioMision> misiones;

    private Boolean baneado;
    private LocalDateTime ultimaRegeneracionVida;

    @ManyToOne
    private Avatar avatarActual;

    private Integer nivel;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "UsuarioAvatar",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "avatar_id")
    )

    private List<Avatar> avataresEnPropiedad = new ArrayList<>();
    
    public Usuario(String nombreUsuario, String email, String password) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.misiones = new ArrayList<>();
        this.vidas = 5;
        this.monedas = 5000;
        this.amigos = new HashSet<>();
        this.baneado = false;
        this.nivel = 1;
        this.activo = false;
        this.experiencia = 0;
        this.copas = 0;
        this.rol = ROL_USUARIO.JUGADOR;
        this.ultimaRegeneracionVida = LocalDateTime.now();
        this.avatarActual = new Avatar("Lia.jpg", 0);
    }

    public Avatar getAvatarActual() {
        return avatarActual;
    }

    public void setAvatarActual(Avatar avatarActual) {
        this.avatarActual = avatarActual;
    }

    public List<Avatar> getAvataresEnPropiedad() {
        return avataresEnPropiedad;
    }

    public void setAvataresEnPropiedad(List<Avatar> avataresEnPropiedad) {
        this.avataresEnPropiedad = avataresEnPropiedad;
    }

    public Integer getCopas() {
        return copas;
    }

    public void setCopas(Integer copas) {
        this.copas = copas;
    }

    public Usuario() {
        this.misiones = new ArrayList<>();
        this.vidas = 5;
        this.monedas = 5000;
        this.amigos = new HashSet<>();
        this.baneado = false;
        this.activo = false;
        this.experiencia = 0;
        this.copas = 0;
        this.nivel = 1;
        this.rol = ROL_USUARIO.JUGADOR;
        this.ultimaRegeneracionVida = LocalDateTime.now();
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Integer getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(Integer experiencia) {
        this.experiencia = experiencia;
    }

    public Integer getVidas() {
        return vidas;
    }

    public void setVidas(Integer vidas) {
        this.vidas = vidas;
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

    public boolean activo() {
        return activo;
    }

    public void activar() {
        activo = true;
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

    public Integer getMonedas() {
        return monedas;
    }

    public void setMonedas(Integer monedas) {
        this.monedas = monedas;
    }

    public Set<Usuario> getAmigos() {
        return amigos;
    }

    public void setAmigos(Set<Usuario> amigos) {
        this.amigos = amigos;
    }

    public Boolean getBaneado() {
        return baneado;
    }

    public void setBaneado(Boolean baneado) {
        this.baneado = baneado;
    }

    public LocalDateTime getUltimaRegeneracionVida() {
        return ultimaRegeneracionVida;
    }

    public void setUltimaRegeneracionVida(LocalDateTime ultimaRegeneracionVida) {
        this.ultimaRegeneracionVida = ultimaRegeneracionVida;
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
}