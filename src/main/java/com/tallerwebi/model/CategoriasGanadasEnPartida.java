package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class CategoriasGanadasEnPartida {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "partida_id")
    private Partida partida;

    @ElementCollection(targetClass = CATEGORIA_PREGUNTA.class)
    @CollectionTable(name = "categorias_ganadas", joinColumns = @JoinColumn(name = "categorias_ganadas_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private Set<CATEGORIA_PREGUNTA> categoriasGanadas = new HashSet<>();

    // Getters y setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Partida getPartida() {
        return partida;
    }
    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Set<CATEGORIA_PREGUNTA> getCategoriasGanadas() {
        return categoriasGanadas;
    }
    public void setCategoriasGanadas(Set<CATEGORIA_PREGUNTA> categoriasGanadas) {
        this.categoriasGanadas = categoriasGanadas;
    }
}


