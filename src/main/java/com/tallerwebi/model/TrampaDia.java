package com.tallerwebi.model;

import javax.persistence.*;

@Entity
@Table(name = "trampa_dia")
public class TrampaDia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dia;

    @ManyToOne
    @JoinColumn(name = "trampa_id")
    private Trampa trampa;

    public TrampaDia() {}

    public TrampaDia(String dia, Trampa trampa) {
        this.dia = dia;
        this.trampa = trampa;
    }

    public Long getId() { return id; }
    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }

    public Trampa getTrampa() { return trampa; }
    public void setTrampa(Trampa trampa) { this.trampa = trampa; }
}
