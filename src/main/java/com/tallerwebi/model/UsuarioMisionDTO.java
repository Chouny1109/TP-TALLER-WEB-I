package com.tallerwebi.model;

public class UsuarioMisionDTO {
    private Long id;
    private String descripcion;
    private Integer progreso;
    private Integer cantidad;
    private Integer experiencia;
    private Integer copas;
    private Boolean completada;
    private Boolean canjeada;

    public UsuarioMisionDTO(Long id, String descripcion, Integer progreso, Integer cantidad, Integer experiencia, Integer copas, Boolean completada, Boolean canjeada) {
        this.id = id;
        this.descripcion = descripcion;
        this.progreso = progreso;
        this.cantidad = cantidad;
        this.experiencia = experiencia;
        this.copas = copas;
        this.completada = completada;
        this.canjeada = canjeada;
    }


    public UsuarioMisionDTO() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getProgreso() {
        return progreso;
    }

    public void setProgreso(Integer progreso) {
        this.progreso = progreso;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(Integer experiencia) {
        this.experiencia = experiencia;
    }

    public Integer getCopas() {
        return copas;
    }

    public void setCopas(Integer copas) {
        this.copas = copas;
    }

    public Boolean getCompletada() {
        return completada;
    }

    public void setCompletada(Boolean completada) {
        this.completada = completada;
    }

    public Boolean getCanjeada() {
        return canjeada;
    }

    public void setCanjeada(Boolean canjeada) {
        this.canjeada = canjeada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
