package com.jotacode.polimarket.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceTestShould {

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService();
    }

    @Test
    public void validar_si_el_formato_del_correo_es_adecuado() {
        String email = "valido@valido.com";
        assertTrue(usuarioService.isValidEmail(email));
    }

    @Test
    public void lanzar_una_excepcion_cuando_el_formato_del_correo_no_es_el_adecuado() {
        String email = "valido@";
        assertFalse(usuarioService.isValidEmail(email));
    }

}