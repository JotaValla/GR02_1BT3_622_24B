package com.jotacode.polimarket.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "valoraciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Valoracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_valoracion")
    private Long idValoracion;

    private Integer estrellas;
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "anuncio_id")
    private Anuncio anun;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuValoracion;

}
