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
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String link;


    @ManyToOne(cascade = CascadeType.ALL)
    private Habilidad Habilidad;


    public Avatar(String nombre, Integer valor) {
        this.valor = valor;
        this.nombre = nombre;
    }

    public Avatar() {
        
    }


    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }
    public Integer getValor() { return valor; }
    public void setValor(Integer valor) { this.valor = valor; }



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public Habilidad getHabilidad() {
        return Habilidad;
    }

    public void setHabilidad(Habilidad habilidad) {
        Habilidad = habilidad;
    }

}
