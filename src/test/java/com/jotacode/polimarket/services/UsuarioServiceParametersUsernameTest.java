package com.jotacode.polimarket.services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsuarioServiceParametersUsernameTest {

    private final UsuarioService usuarioService = new UsuarioService();

    // Método que provee los parámetros para el test
    public static Iterable<Object[]> parameters() {
        List<Object[]> objects = new ArrayList<>();
        objects.add(new Object[]{"usuario123", true});  // Válido
        objects.add(new Object[]{"user", true});       // Válido
        objects.add(new Object[]{"u1", false});        // Menos de 3 caracteres
        objects.add(new Object[]{"usuario@dominio", false}); // Contiene un carácter especial (@)
        objects.add(new Object[]{"usuario$", false});  // Contiene un carácter especial ($)
        objects.add(new Object[]{" ", false});         // Espacio en blanco
        objects.add(new Object[]{"nombre123", true});  // Válido
        return objects;
    }

    // Test parametrizado que toma los nombres de usuario de la fuente
    @ParameterizedTest
    @MethodSource("parameters")
    public void validar_si_el_nombre_de_usuario_es_valido(String username, boolean resultadoEsperado) {
        assertEquals(resultadoEsperado, usuarioService.validarNombre(username));
    }
}
