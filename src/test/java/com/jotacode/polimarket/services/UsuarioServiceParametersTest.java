package com.jotacode.polimarket.services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsuarioServiceParametersTest {

    private final UsuarioService usuarioService = new UsuarioService();

    // Método que provee los parámetros para el test
    public static Iterable<Object[]> parameters() {
        List<Object[]> objects = new ArrayList<>();
        objects.add(new Object[]{"usuario@dominio.com", true});  // Caso de email válido
        objects.add(new Object[]{"nombre.apellido@dominio.com", true});
        objects.add(new Object[]{"usuario@dominio", false});     // Falta el dominio de nivel superior
        objects.add(new Object[]{"@dominio.com", false});        // Falta el nombre de usuario
        objects.add(new Object[]{"usuario@@dominio.com", false}); // Doble arroba
        objects.add(new Object[]{"usuario@dominio..com", false}); // Doble punto
        objects.add(new Object[]{"usuario@dominio.com ", false}); // Espacio al final
        return objects;
    }

    // Test parametrizado que toma los correos de la fuente
    @ParameterizedTest
    @MethodSource("parameters")
    public void validar_si_el_correo_es_valido(String email, boolean resultadoEsperado) {
        assertEquals(resultadoEsperado, usuarioService.isValidEmail(email));
    }
}