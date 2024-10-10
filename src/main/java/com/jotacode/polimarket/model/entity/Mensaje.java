package com.jotacode.polimarket.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mensaje {

    private Long id;
    private Usuario emisor;
    private String contenido;
    private Chat chat;

}
