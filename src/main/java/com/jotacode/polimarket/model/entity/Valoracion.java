package com.jotacode.polimarket.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Valoracion ")
public class Valoracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idvaloracion;

    private Integer estrellas;


    private String comentario;

    // Definir la relación con la entidad Anuncio (por ejemplo ManyToOne)
    @ManyToOne(targetEntity = Anuncio.class, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "anuncio_id")
    private Anuncio anuncio;

    // Getters y Setters
    public Long getIdvaloracion() {
        return idvaloracion;
    }

    public void setIdvaloracion(Long idvaloracion) {
        this.idvaloracion = idvaloracion;
    }

    public Integer getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(Integer estrellas) {
        this.estrellas = estrellas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }
}
