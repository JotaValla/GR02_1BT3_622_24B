package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.entity.Valoracion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValoracionServiceShould {

    private ValoracionService valoracionService;

    @BeforeEach
    public void setUp() {
        valoracionService = new ValoracionService();
    }

    //Pruebas unitarias
    @Test
    public void aceptar_la_creacion_de_una_valoracion_con_todos_sus_parametros() {
        Valoracion valoracion = valoracionService.crearValoracion(4, "me gusto mucho la comida de este lugar, bastante fresco y menu bariado");
        assertNotNull(valoracion);
        assertEquals(4, valoracion.getEstrellas());
        assertEquals("me gusto mucho la comida de este lugar, bastante fresco y menu bariado", valoracion.getComentario());
    }

    @Test
    public void lanzar_excepcion_si_un_parametro_de_la_valoracion_es_nulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            valoracionService.crearValoracion(3, null);
        });
    }

    @Test
    public void lanzar_exepcion_cuando_se_crea_una_valoracion_con_una_calificacion_mayor_a_5() {
        assertThrows(IllegalArgumentException.class, () -> {
            valoracionService.crearValoracion(6, "calificacion invalida");
        });
    }

    @Test
    public void lanzar_excepcion_si_anuncio_o_usuario_o_valoracion_son_nulos_al_vincularlos() {
        Valoracion valoracion = valoracionService.crearValoracion(4, "los churros estaban feos :c");
        assertThrows(IllegalArgumentException.class, () -> {
            valoracionService.vincularValoracion(valoracion, null, null);
        });
    }

    @Test
    public void no_permitir_comentario_nulo_en_valoracion() {
        assertThrows(IllegalArgumentException.class, () -> {
            valoracionService.crearValoracion(3, null);
        }, "Debería lanzar una excepción si el comentario es nulo.");
    }

    @Test
    public void no_permitir_valoracion_con_estrellas_invalidas() {
        assertThrows(IllegalArgumentException.class, () -> {
            valoracionService.crearValoracion(6, "Excesivo puntaje");
        }, "No debería permitir calificación mayor a 5.");

        assertThrows(IllegalArgumentException.class, () -> {
            valoracionService.crearValoracion(-1, "Puntaje negativo");
        }, "No debería permitir calificación negativa.");
    }

    @Test
    public void crear_valoracion_dentro_de_rango_valido() {
        Valoracion valoracion = valoracionService.crearValoracion(4, "Buen servicio");
        assertEquals(4, valoracion.getEstrellas(), "La valoración debería ser de 4 estrellas.");
    }


}
