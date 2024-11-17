package com.jotacode.polimarket.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
    private int vistas = 0; // Inicializamos el conteo de vistas en 0
    
    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuAnuncio;

    @OneToMany(mappedBy = "anun")
    private List<Valoracion> valoraciones;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Anuncio anuncio = (Anuncio) o;
        return Objects.equals(idAnuncio, anuncio.idAnuncio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAnuncio);
    }
}
