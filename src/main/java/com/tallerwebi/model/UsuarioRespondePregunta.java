package com.tallerwebi.model;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "pregunta_id"})
)
public class UsuarioRespondePregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pregunta_id")
    private Pregunta pregunta;


    public UsuarioRespondePregunta() {

    }

    public UsuarioRespondePregunta(Usuario usuario, Pregunta pregunta) {
        this.usuario = usuario;
        this.pregunta = pregunta;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Usuario getUsuario() {
        return usuario;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }
    public Pregunta getPregunta() {
        return pregunta;
    }
}
