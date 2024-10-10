package com.jotacode.polimarket.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

    private Long id;
    private Anuncio anuncio;
    private Usuario comprador;
    private List<Mensaje> mensajes;
}
