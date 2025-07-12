package com.tallerwebi.model;

public class UsuarioDTO {
    private Long id;
    private String nombreUsuario;
    private Integer nivel;
    private String linkAvatar;
    private String email;

    public UsuarioDTO(Usuario usuario, String linkAvatar) {
        this.id = usuario.getId();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.linkAvatar = linkAvatar;
    }

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.email = usuario.getEmail();
    }

    public UsuarioDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getLinkAvatar() {
        return linkAvatar;
    }

    public void setLinkAvatar(String linkAvatar) {
        this.linkAvatar = linkAvatar;
    }

}
