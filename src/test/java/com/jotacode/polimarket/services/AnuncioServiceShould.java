package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.AnuncioDAO;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnuncioServiceShould {

    private AnuncioService anuncioService;

    @BeforeEach
    public void setUp() {
        anuncioService = new AnuncioService();
    }

    @Test
    public void aceptar_la_creacion_de_un_anuncio_con_todos_sus_parametros() {
        Anuncio anuncio = anuncioService.crearAnuncio("Laptop", "Laptop en buen estado", "laptop.jpg", "Electronica", new BigDecimal("1000"));

        assertNotNull(anuncio);
        assertEquals("Laptop", anuncio.getTitulo());
        assertEquals("Laptop en buen estado", anuncio.getDescripcion());
        assertEquals("laptop.jpg", anuncio.getImagen());
        assertEquals("Electronica", anuncio.getCategoria());
        assertEquals(new BigDecimal("1000"), anuncio.getPrecio());
    }

    @Test
    public void lanzar_excepcion_si_un_parametro_del_anuncio_es_nulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            anuncioService.crearAnuncio(null, "Laptop en buen estado", "laptop.jpg", "Electronica", new BigDecimal("1000"));
        });
    }

    @Test
    public void lanzar_excepcion_si_anuncio_o_usuario_son_nulos_al_vincularlos() {
        Anuncio anuncio = new Anuncio();
        assertThrows(IllegalArgumentException.class, () -> {
            anuncioService.vincularAnuncioConUsuario(anuncio, null);
        });
    }


    @Test
    public void testFindAnunciosByCategoria() {
        // Mock del AnuncioDAO
        AnuncioDAO anuncioDAOMock = Mockito.mock(AnuncioDAO.class);
        // Lista simulada de anuncios
        List<Anuncio> anunciosSimulados = Arrays.asList(new Anuncio(), new Anuncio());

        // Mockear el comportamiento del método findAnunciosByCategoria
        when(anuncioDAOMock.findAnunciosByCategoria("Electronica")).thenReturn(anunciosSimulados);

        // Reemplazar el anuncioDAO en el servicio con el mock
        anuncioService = new AnuncioService();
        anuncioService.anuncioDAO = anuncioDAOMock;

        // Llamar al método
        List<Anuncio> anuncios = anuncioService.findAnunciosByCategoria("Electronica");

        // Verificar que el método del DAO fue llamado
        verify(anuncioDAOMock, times(1)).findAnunciosByCategoria("Electronica");

        // Asegurarse de que los resultados son los esperados
        assertEquals(anunciosSimulados, anuncios);
    }

    @Test
    public void testFindById() {
        // Mock del AnuncioDAO
        AnuncioDAO anuncioDAOMock = Mockito.mock(AnuncioDAO.class);
        Anuncio anuncioSimulado = new Anuncio();
        anuncioSimulado.setIdAnuncio(1L);

        // Mockear el comportamiento del método find
        when(anuncioDAOMock.find(1L)).thenReturn(anuncioSimulado);

        // Reemplazar el anuncioDAO en el servicio con el mock
        anuncioService = new AnuncioService();
        anuncioService.anuncioDAO = anuncioDAOMock;

        // Llamar al método
        Anuncio anuncio = anuncioService.findById(1L);

        // Verificar que el método del DAO fue llamado
        verify(anuncioDAOMock, times(1)).find(1L);

        // Asegurarse de que el anuncio es el correcto
        assertEquals(anuncioSimulado, anuncio);
    }


}