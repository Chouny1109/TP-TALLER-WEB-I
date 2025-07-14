package com.tallerwebi.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "trampa_reclamada")
public class TrampaReclamada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Trampa trampa;

    private LocalDate fecha;

    public TrampaReclamada() {}

    public TrampaReclamada(Usuario usuario, Trampa trampa, LocalDate fecha) {
        this.usuario = usuario;
        this.trampa = trampa;
        this.fecha = fecha;
    }

    public Long getId() { return id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Trampa getTrampa() { return trampa; }
    public void setTrampa(Trampa trampa) { this.trampa = trampa; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}
