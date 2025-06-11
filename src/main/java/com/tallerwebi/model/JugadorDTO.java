package com.tallerwebi.model;

public class JugadorDTO {

    private Long id;
    private String nombreUsuario;
    private String email;
    private String linkAvatar;

    public JugadorDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.email = usuario.getEmail();
    }

    @Override
    public String toString() {
        return "JugadorDTO{" +
                "id=" + id +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", email='" + email + '\'' +
                ", linkAvatar='" + linkAvatar + '\'' +
                '}';
    }

    public void setLinkAvatar(String linkAvatar) {

        this.linkAvatar = linkAvatar;
    }
    public String getLinkAvatar() {
        return linkAvatar;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

