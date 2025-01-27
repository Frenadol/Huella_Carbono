package org.example.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "factor_emision", nullable = false)
    private Float factorEmision;

    @Column(name = "unidad", nullable = false, length = 20)
    private String unidad;

    @OneToMany(mappedBy = "idCategoria")
    private List<Actividad> actividades = new ArrayList<>();

    @OneToMany(mappedBy = "idCategoria")
    private List<Recomendacion> recomendaciones =new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Float getFactorEmision() {
        return factorEmision;
    }

    public void setFactorEmision(Float factorEmision) {
        this.factorEmision = factorEmision;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public List<Actividad> getActividad() {
        return actividades;
    }

    public void setActividads(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public List<Recomendacion> getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendacions(List<Recomendacion> recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

}