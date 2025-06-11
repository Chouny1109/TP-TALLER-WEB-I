package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.ESTADO_AVATAR;

import javax.persistence.*;

@Entity
public class UsuarioAvatar {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Usuario usuario;

    @ManyToOne(cascade = CascadeType.ALL)
    private Avatar avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadoAvatar")
    private ESTADO_AVATAR estado;

    public UsuarioAvatar() {}

    public void setEstado(ESTADO_AVATAR estado) {
        this.estado = estado;
    }

    public ESTADO_AVATAR getEstado() {
        return estado;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Avatar getAvatar() {
        return avatar;
    }
    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
