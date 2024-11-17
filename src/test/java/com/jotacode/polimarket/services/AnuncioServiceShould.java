package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.AnuncioDAO;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnuncioServiceShould {

    private AnuncioService anuncioService;
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        anuncioService = new AnuncioService();
        usuarioService = new UsuarioService();
    }

    //Tests unitarios
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

    //Iteracion 4 tests
    @Test
    public void lanzar_excepcion_si_el_precio_es_negativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            anuncioService.crearAnuncio("Producto", "Descripcion", "imagen.jpg", "Categoria", new BigDecimal("-10"));
        });
    }


    //TEST CON MOCKITO
    @Test
    public void testFindAnunciosByCategoria() {
        // Mock del AnuncioDAO
        AnuncioDAO anuncioDAOMock = Mockito.mock(AnuncioDAO.class);
        // Lista simulada de anuncios
        List<Anuncio> anunciosSimulados = Arrays.asList(new Anuncio(), new Anuncio());

        // Mockear el comportamiento del método findAnunciosByCategoria
        Mockito.when(anuncioDAOMock.findAnunciosByCategoria("Electronica")).thenReturn(anunciosSimulados);

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

    //TEST CON MOCKITO
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

    @Test
    public void calcular_promedio_valoraciones_correctamente() {
        // Mock del AnuncioDAO
        AnuncioDAO anuncioDAOMock = mock(AnuncioDAO.class);

        // Crear un anuncio con valoraciones simuladas
        Anuncio anuncio = new Anuncio();
        anuncio.setIdAnuncio(1L);

        Valoracion valoracion1 = new Valoracion();
        valoracion1.setEstrellas(4);
        Valoracion valoracion2 = new Valoracion();
        valoracion2.setEstrellas(5);
        Valoracion valoracion3 = new Valoracion();
        valoracion3.setEstrellas(3);

        anuncio.setValoraciones(Arrays.asList(valoracion1, valoracion2, valoracion3));

        // Mockear el comportamiento del método findByIdWithValoraciones
        when(anuncioDAOMock.findByIdWithValoraciones(1L)).thenReturn(anuncio);

        // Reemplazar el anuncioDAO en el servicio con el mock
        anuncioService.anuncioDAO = anuncioDAOMock;

        // Llamar al método
        double promedio = anuncioService.calcularPromedioValoraciones(1L);

        // Verificar el cálculo del promedio
        assertEquals(4.0, promedio, 0.01, "El promedio de valoraciones debería ser 4.0");
    }

    @Test
    public void calcular_promedio_valoraciones_sin_valoraciones() {
        // Mock del AnuncioDAO
        AnuncioDAO anuncioDAOMock = mock(AnuncioDAO.class);

        // Crear un anuncio sin valoraciones
        Anuncio anuncio = new Anuncio();
        anuncio.setIdAnuncio(2L);
        anuncio.setValoraciones(new ArrayList<>());

        // Mockear el comportamiento del método findByIdWithValoraciones
        when(anuncioDAOMock.findByIdWithValoraciones(2L)).thenReturn(anuncio);

        // Reemplazar el anuncioDAO en el servicio con el mock
        anuncioService.anuncioDAO = anuncioDAOMock;

        // Llamar al método
        double promedio = anuncioService.calcularPromedioValoraciones(2L);

        // Verificar que el promedio es 0.0 cuando no hay valoraciones
        assertEquals(0.0, promedio, "El promedio debería ser 0.0 cuando no hay valoraciones");
    }


    // TEST PARAMETRIZADO PARA findAnunciosByCategoria
    @ParameterizedTest
    @CsvSource({
            "Electronica, 3",
            "Vehiculos, 2",
            "Ropa, 1"
    })
    public void testFindAnunciosByCategoriaParametrized(String categoria, int expectedSize) {
        // Mock del AnuncioDAO
        AnuncioDAO anuncioDAOMock = mock(AnuncioDAO.class);

        // Crear una lista simulada de anuncios de tamaño expectedSize
        List<Anuncio> anunciosSimulados = new ArrayList<>();
        for (int i = 0; i < expectedSize; i++) {
            anunciosSimulados.add(new Anuncio());
        }

        // Mockear el comportamiento del método findAnunciosByCategoria
        when(anuncioDAOMock.findAnunciosByCategoria(categoria)).thenReturn(anunciosSimulados);

        // Reemplazar el DAO en el servicio con el mock
        anuncioService.anuncioDAO = anuncioDAOMock;

        // Llamar al método con el parámetro de categoría
        List<Anuncio> anuncios = anuncioService.findAnunciosByCategoria(categoria);

        // Verificar que el método fue llamado correctamente
        verify(anuncioDAOMock, times(1)).findAnunciosByCategoria(categoria);

        // Verificar que el tamaño de la lista sea el esperado
        assertEquals(expectedSize, anuncios.size());
    }

    // TEST PARAMETRIZADO PARA findById
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    public void testFindByIdParametrized(Long id) {
        // Mock del AnuncioDAO
        AnuncioDAO anuncioDAOMock = mock(AnuncioDAO.class);
        Anuncio anuncioSimulado = new Anuncio();
        anuncioSimulado.setIdAnuncio(id);

        // Mockear el comportamiento del método find
        when(anuncioDAOMock.find(id)).thenReturn(anuncioSimulado);

        // Reemplazar el DAO en el servicio con el mock
        anuncioService.anuncioDAO = anuncioDAOMock;

        // Llamar al método con el parámetro de ID
        Anuncio anuncio = anuncioService.findById(id);

        // Verificar que el método fue llamado correctamente
        verify(anuncioDAOMock, times(1)).find(id);

        // Verificar que el anuncio retornado es el correcto
        assertEquals(id, anuncio.getIdAnuncio());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-10", "-5", "-1"})
    public void lanzar_excepcion_si_precio_es_negativo(String precioStr) {
        BigDecimal precio = new BigDecimal(precioStr);
        assertThrows(IllegalArgumentException.class, () -> {
            anuncioService.crearAnuncio("Producto", "Descripción", "imagen.jpg", "Categoría", precio);
        }, "Debería lanzar una excepción si el precio es negativo");
    }

}