package com.tallerwebi.model;

import com.tallerwebi.dominio.enums.ESTADO_AVATAR;

import javax.persistence.*;

@Entity
@Table(name = "Avatar")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer valor;

    @ManyToOne(cascade = CascadeType.ALL)
    private Habilidad Habilidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadoAvatar")
    private ESTADO_AVATAR estado = ESTADO_AVATAR.BLOQUEADO;

    public Avatar(String nombre, Integer valor) {
        this.valor = valor;
        this.nombre = nombre;
    }

    public Avatar() {
        
    }

    public String getNombre() { return nombre; }
    public Integer getValor() { return valor; }
    public ESTADO_AVATAR getEstado() { return estado; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setValor(Integer valor) { this.valor = valor; }

    public void setEstado(ESTADO_AVATAR estado) { this.estado = estado; }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
