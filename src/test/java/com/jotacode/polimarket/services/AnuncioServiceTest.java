package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.AnuncioDAO;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AnuncioServiceTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private AnuncioService anuncioService;
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        // Configuración de EntityManager y servicio
        emf = Persistence.createEntityManagerFactory("PolimarketPU-Test");
        em = emf.createEntityManager();
        anuncioService = new AnuncioService();
        anuncioService.anuncioDAO = new AnuncioDAO(emf, Anuncio.class);

        em.getTransaction().begin();

        // Crear y persistir un usuario
        usuario = new Usuario();
        usuario.setNombre("Usuario de Prueba");
        usuario.setTelefono("123456789");
        usuario.setEmail("usuario@prueba.com");
        em.persist(usuario);

        em.getTransaction().commit();
    }

    @AfterEach
    public void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback(); // Asegura que las transacciones abiertas se cierren
        }
        if (em != null) {
            em.close(); // Cierra el EntityManager
        }
        if (emf != null) {
            emf.close(); // Cierra el EntityManagerFactory
        }
    }

    @Test
    public void calcular_promedio_valoraciones_exitosamente() {
        em.getTransaction().begin();
        // Crear y persistir un anuncio
        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo("Anuncio con Valoraciones");
        anuncio.setDescripcion("Descripción de prueba");
        anuncio.setCategoria("Prueba");
        anuncio.setPrecio(BigDecimal.valueOf(100.00));
        anuncio.setUsuAnuncio(usuario);
        em.persist(anuncio);
        // Crear y persistir valoraciones
        Valoracion valoracion1 = new Valoracion();
        valoracion1.setEstrellas(5);
        valoracion1.setComentario("Excelente");
        valoracion1.setUsuValoracion(usuario);
        valoracion1.setAnun(anuncio);
        em.persist(valoracion1);
        Valoracion valoracion2 = new Valoracion();
        valoracion2.setEstrellas(3);
        valoracion2.setComentario("Regular");
        valoracion2.setUsuValoracion(usuario);
        valoracion2.setAnun(anuncio);
        em.persist(valoracion2);
        Valoracion valoracion3 = new Valoracion();
        valoracion3.setEstrellas(4);
        valoracion3.setComentario("Bueno");
        valoracion3.setUsuValoracion(usuario);
        valoracion3.setAnun(anuncio);
        em.persist(valoracion3);
        em.getTransaction().commit();
        // Usar el servicio para calcular el promedio de valoraciones
        double promedio = anuncioService.calcularPromedioValoraciones(anuncio.getIdAnuncio());
        // Validar el resultado
        assertEquals(4.0, promedio, 0.01, "El promedio de valoraciones debería ser 4.0");
    }

    @Test
    public void calcular_promedio_valoraciones_sin_valoraciones() {
        em.getTransaction().begin();

        // Crear y persistir un anuncio sin valoraciones
        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo("Anuncio sin Valoraciones");
        anuncio.setDescripcion("Descripción de prueba");
        anuncio.setCategoria("Prueba");
        anuncio.setPrecio(BigDecimal.valueOf(100.00));
        anuncio.setUsuAnuncio(usuario);
        em.persist(anuncio);

        em.getTransaction().commit();

        // Usar el servicio para calcular el promedio de valoraciones
        double promedio = anuncioService.calcularPromedioValoraciones(anuncio.getIdAnuncio());

        // Validar el resultado
        assertEquals(0.0, promedio, "El promedio de valoraciones debería ser 0.0");
    }

    @Test
    public void crear_anuncio_con_precio_negativo_deberia_lanzar_excepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            anuncioService.crearAnuncio("Título", "Descripción", "imagen.jpg", "Categoría", BigDecimal.valueOf(-50.00));
        });

        assertEquals("El precio no puede ser negativo", exception.getMessage());
    }

    @Test
    public void crear_anuncio_con_campos_nulos_deberia_lanzar_excepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            anuncioService.crearAnuncio(null, null, null, null, null);
        });

        assertEquals("Todos los campos deben ser llenados", exception.getMessage());
    }

    @Test
    public void vincular_anuncio_con_usuario_nulo_deberia_lanzar_excepcion() {
        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo("Anuncio Prueba");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            anuncioService.vincularAnuncioConUsuario(anuncio, null);
        });

        assertEquals("El anuncio y el usuario no pueden ser nulos", exception.getMessage());
    }

    @Test
    public void vincular_anuncio_nulo_con_usuario_deberia_lanzar_excepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            anuncioService.vincularAnuncioConUsuario(null, usuario);
        });

        assertEquals("El anuncio y el usuario no pueden ser nulos", exception.getMessage());
    }

    @Test
    public void find_anuncio_por_id_inexistente_deberia_retornar_null() {
        Long idInexistente = 999L; // ID que no existe en la base

        Anuncio anuncio = anuncioService.findById(idInexistente);

        assertNull(anuncio, "El anuncio con ID inexistente debería ser null.");
    }

    @Test
    public void find_anuncios_por_categoria_inexistente_deberia_retornar_lista_vacia() {
        String categoriaInexistente = "Categoría Inexistente";

        List<Anuncio> anuncios = anuncioService.findAnunciosByCategoria(categoriaInexistente);

        assertNotNull(anuncios, "La lista de anuncios no debería ser nula.");
        assertTrue(anuncios.isEmpty(), "La lista de anuncios debería estar vacía.");
    }

    // TEST PARAMETRIZADO PARA
    @ParameterizedTest
    @CsvSource({
            "'5,4,3', 4.0",   // Promedio de 5, 4 y 3 es 4.0
            "'1,2,3,4,5', 3.0", // Promedio de 1, 2, 3, 4 y 5 es 3.0
            "'3', 3.0",       // Solo una valoración con 3
            "'', 0.0"         // Sin valoraciones, promedio es 0.0
    })
    public void calcular_promedio_valoraciones_parametrizado(String valoracionesInput, double promedioEsperado) {
        em.getTransaction().begin();
        // Crear y persistir un anuncio
        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo("Anuncio Parametrizado");
        anuncio.setDescripcion("Descripción del anuncio parametrizado");
        anuncio.setCategoria("Test");
        anuncio.setPrecio(BigDecimal.valueOf(100.00));
        anuncio.setUsuAnuncio(usuario);
        em.persist(anuncio);
        // Crear valoraciones si el input no está vacío
        if (!valoracionesInput.isEmpty()) {
            String[] estrellasArray = valoracionesInput.split(",");
            for (String estrellas : estrellasArray) {
                Valoracion valoracion = new Valoracion();
                valoracion.setEstrellas(Integer.parseInt(estrellas.trim()));
                valoracion.setComentario("Valoración de prueba");
                valoracion.setUsuValoracion(usuario);
                valoracion.setAnun(anuncio);
                em.persist(valoracion);
            }
        }
        em.getTransaction().commit();
        // Llamar al servicio para calcular el promedio
        double promedioCalculado = anuncioService.calcularPromedioValoraciones(anuncio.getIdAnuncio());
        // Validar el resultado
        assertEquals(promedioEsperado, promedioCalculado, 0.01, "El promedio calculado no coincide con el esperado.");
    }


}
