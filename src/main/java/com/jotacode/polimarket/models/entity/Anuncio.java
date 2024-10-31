package com.jotacode.polimarket.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ANUNCIOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_anuncio")
    private Long idAnuncio;

    private String titulo;
    private String descripcion;
    private String imagen;
    private String categoria;
    private BigDecimal precio;

    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuAnuncio;

    @OneToMany(mappedBy = "anun")
    private List<Valoracion> valoraciones;

}
