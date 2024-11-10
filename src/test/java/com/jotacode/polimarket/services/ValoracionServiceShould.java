package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.ValoracionDAO;
import com.jotacode.polimarket.models.entity.Valoracion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ValoracionServiceShould {

    private ValoracionService valoracionService;
    private ValoracionDAO valoracionDAOMock;

    @BeforeEach
    public void setUp() {
        valoracionDAOMock = mock(ValoracionDAO.class);
        valoracionService = new ValoracionService();
        valoracionService.valoracionDAO = valoracionDAOMock;
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

    //Pruebas con mock
    // Test para actualizar una valoración
    @Test
    public void actualizar_una_valoracion_correctamente() {
        Valoracion valoracionExistente = new Valoracion();
        valoracionExistente.setIdValoracion(1L);
        valoracionExistente.setEstrellas(3);
        valoracionExistente.setComentario("Comentario antiguo");

        when(valoracionDAOMock.findById(1L)).thenReturn(valoracionExistente);

        valoracionService.updateValoracion(1L, 4, "Comentario actualizado");

        verify(valoracionDAOMock, times(1)).updateValoracion(valoracionExistente);
        assertEquals(4, valoracionExistente.getEstrellas());
        assertEquals("Comentario actualizado", valoracionExistente.getComentario());
    }

    @Test
    public void lanzar_excepcion_si_valoracion_a_actualizar_no_existe() {
        when(valoracionDAOMock.findById(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            valoracionService.updateValoracion(1L, 4, "Comentario actualizado");
        }, "Debería lanzar una excepción si la valoración no existe");
    }

    // Test para encontrar una valoración por ID
    @Test
    public void encontrar_valoracion_por_id() {
        Valoracion valoracion = new Valoracion();
        valoracion.setIdValoracion(1L);

        when(valoracionDAOMock.findById(1L)).thenReturn(valoracion);

        Valoracion result = valoracionService.findById(1L);

        assertNotNull(result, "La valoración debería encontrarse");
        assertEquals(1L, result.getIdValoracion());
        verify(valoracionDAOMock, times(1)).findById(1L);
    }

    // Test para encontrar valoraciones por usuario
    @Test
    public void encontrar_valoraciones_por_usuario() {
        List<Valoracion> valoraciones = Arrays.asList(new Valoracion(), new Valoracion());

        when(valoracionDAOMock.findValoracionesByUsuario(1L)).thenReturn(valoraciones);

        List<Valoracion> result = valoracionService.findValoracionesByUsuario(1L);

        assertEquals(2, result.size(), "Debería encontrar dos valoraciones para el usuario");
        verify(valoracionDAOMock, times(1)).findValoracionesByUsuario(1L);
    }

    // Test para encontrar valoraciones por anuncio
    @Test
    public void encontrar_valoraciones_por_anuncio() {
        List<Valoracion> valoraciones = Arrays.asList(new Valoracion(), new Valoracion(), new Valoracion());

        when(valoracionDAOMock.findValoracionesByAnuncio(1L)).thenReturn(valoraciones);

        List<Valoracion> result = valoracionService.findValoracionesByAnuncio(1L);

        assertEquals(3, result.size(), "Debería encontrar tres valoraciones para el anuncio");
        verify(valoracionDAOMock, times(1)).findValoracionesByAnuncio(1L);
    }

    // Test para verificar si existe una valoración de usuario para un anuncio específico
    @Test
    public void verificar_existencia_de_valoracion_de_usuario_para_anuncio() {
        Valoracion valoracion = new Valoracion();
        when(valoracionDAOMock.findValoracionByUsuarioAndAnuncio(1L, 1L)).thenReturn(valoracion);

        boolean existe = valoracionService.existeValoracionDeUsuarioParaAnuncio(1L, 1L);

        assertTrue(existe, "Debería existir una valoración para este usuario y anuncio");
        verify(valoracionDAOMock, times(1)).findValoracionByUsuarioAndAnuncio(1L, 1L);
    }

    @Test
    public void retornar_falso_si_no_existe_valoracion_de_usuario_para_anuncio() {
        when(valoracionDAOMock.findValoracionByUsuarioAndAnuncio(1L, 1L)).thenReturn(null);

        boolean existe = valoracionService.existeValoracionDeUsuarioParaAnuncio(1L, 1L);

        assertFalse(existe, "No debería existir una valoración para este usuario y anuncio");
        verify(valoracionDAOMock, times(1)).findValoracionByUsuarioAndAnuncio(1L, 1L);
    }

    //Pruebas parametrizadas
    @ParameterizedTest
    @CsvSource({
            "5, Excelente servicio",
            "3, Servicio promedio",
            "1, Muy mal servicio"
    })
    public void crear_valoracion_con_estrellas_y_comentario(int estrellas, String comentario) {
        Valoracion valoracion = valoracionService.crearValoracion(estrellas, comentario);

        assertNotNull(valoracion);
        assertEquals(estrellas, valoracion.getEstrellas());
        assertEquals(comentario, valoracion.getComentario());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 6})
    public void lanzar_excepcion_si_estrellas_fuera_de_rango(int estrellas) {
        assertThrows(IllegalArgumentException.class, () -> {
            valoracionService.crearValoracion(estrellas, "Comentario de prueba");
        }, "Debería lanzar una excepción si las estrellas están fuera del rango 1-5");
    }

}
