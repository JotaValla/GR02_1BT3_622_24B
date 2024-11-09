package com.jotacode.polimarket.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
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

    private String nombre;
    private String foto;
    private String telefono;
    private String email;

    @OneToMany(mappedBy = "usuAnuncio")
    private List<Anuncio> anuncios;

    @OneToMany(mappedBy = "usuValoracion")
    private List<Valoracion> valoraciones;

    @OneToOne
    @JoinColumn(name = "cuenta_id", referencedColumnName = "id_cuenta")
    private Cuenta cuenta;

    @ManyToMany
    @JoinTable(
            name = "USUARIO_FAVORITOS",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "anuncio_id")
    )
    private List<Anuncio> favoritos;
}
