package com.jotacode.polimarket.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "USUARIOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    private String username;
    private String foto;
    private String telefono;
    private String email;

    @OneToMany(mappedBy = "usuAnuncio")
    private List<Anuncio> anuncios;

    @OneToMany(mappedBy = "usuValoracion")
    private List<Valoracion> valoraciones;

}
