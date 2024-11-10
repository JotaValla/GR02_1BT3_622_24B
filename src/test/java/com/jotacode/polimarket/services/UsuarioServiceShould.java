package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceShould {

    private UsuarioService usuarioService;


    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService();
    }

    //Pruebas unitarias iteracion 1, 2 y 3

    @Test
    public void no_permitir_nombre_vacio_o_nulo() {
        assertFalse(usuarioService.validarNombre(""), "El nombre vacío no debería ser válido.");
        assertFalse(usuarioService.validarNombre(null), "El nombre nulo no debería ser válido.");
    }

    @Test
    public void permitir_nombre_con_espacios_y_tildes() {
        assertTrue(usuarioService.validarNombre("José Pérez"), "El nombre con espacios y tildes debería ser válido.");
    }

    @Test
    public void validar_correo_con_subdominios() {
        String email = "usuario.personal@sub.dominio.com";
        assertTrue(usuarioService.validarEmail(email), "El correo con subdominios debería ser válido.");
    }

    @Test
    public void no_permitir_correo_con_caracteres_especiales_antes_del_arroba() {
        String email = "user#name@domain.com";
        assertFalse(usuarioService.validarEmail(email), "El correo con caracteres especiales antes del @ no debería ser válido.");
    }


    @Test
    public void validar_si_el_formato_del_correo_es_adecuado() {
        String email = "valido@valido.com";
        assertTrue(usuarioService.validarEmail(email));
    }

    @Test
    public void lanzar_una_excepcion_cuando_el_formato_del_correo_no_es_el_adecuado() {
        String email = "valido@";
        assertFalse(usuarioService.validarEmail(email));
    }

    //Pruebas parametrizadas

    // Método que provee los parámetros para el test 2
    public static Iterable<Object[]> parameters() {
        List<Object[]> objects = new ArrayList<>();
        objects.add(new Object[]{"usuario123", false});
        objects.add(new Object[]{"user", true});
        objects.add(new Object[]{"u1", false});
        objects.add(new Object[]{"usuario@dominio", false});
        objects.add(new Object[]{"usuario$", false});
        objects.add(new Object[]{" ", false});
        objects.add(new Object[]{"nombre123", false});
        return objects;
    }

    // Test parametrizado que toma los nombres de usuario de la fuente
    @ParameterizedTest
    @MethodSource("parameters")
    public void validar_si_el_nombre_es_valido(String username, boolean resultadoEsperado) {
        assertEquals(resultadoEsperado, usuarioService.validarNombre(username));
    }

    // Método que provee los parámetros para el test 2
    public static Iterable<Object[]> parameters2() {
        List<Object[]> objects = new ArrayList<>();
        objects.add(new Object[]{"usuario@dominio.com", true});  // Caso de email válido
        objects.add(new Object[]{"nombre.apellido@dominio.com", true});
        objects.add(new Object[]{"usuario@dominio", false});     // Falta el dominio de nivel superior
        objects.add(new Object[]{"@dominio.com", false});        // Falta el nombre de usuario
        objects.add(new Object[]{"usuario@@dominio.com", false}); // Doble arroba
        objects.add(new Object[]{"usuario@dominio.com ", false}); // Espacio al final
        return objects;
    }

    // Test parametrizado que toma los correos de la fuente
    @ParameterizedTest
    @MethodSource("parameters2")
    public void validar_si_el_correo_es_valido(String email, boolean resultadoEsperado) {
        assertEquals(resultadoEsperado, usuarioService.validarEmail(email));
    }

}