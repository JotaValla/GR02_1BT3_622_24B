package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.UsuarioDAO;
import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.models.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private UsuarioService usuarioService;
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        // Configuración de EntityManager y servicio
        emf = Persistence.createEntityManagerFactory("PolimarketPU-Test");
        em = emf.createEntityManager();
        usuarioService = new UsuarioService();
        usuarioService.usuarioDAO = new UsuarioDAO(emf, Usuario.class);

        em.getTransaction().begin();

        // Crear y persistir una cuenta
        Cuenta cuenta = new Cuenta();
        cuenta.setUsername("usuario_prueba");
        cuenta.setPassword("password123");
        em.persist(cuenta);

        // Crear y persistir un usuario asociado a la cuenta
        usuario = new Usuario();
        usuario.setNombre("Usuario de Prueba");
        usuario.setTelefono("123456789");
        usuario.setEmail("usuario@prueba.com");
        usuario.setCuenta(cuenta);
        usuario.setFavoritos(new ArrayList<>());
        em.persist(usuario);

        // Asociar la cuenta al usuario
        cuenta.setUsuario(usuario);
        em.merge(cuenta);
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
    public void agregar_anuncio_a_favoritos_exitosamente_usando_servicio() throws NonexistentEntityException {
        em.getTransaction().begin();

        // Crear y persistir un anuncio
        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo("Anuncio de prueba");
        anuncio.setDescripcion("Descripción del anuncio de prueba");
        anuncio.setCategoria("Categoría de prueba");
        anuncio.setPrecio(BigDecimal.valueOf(99.99));
        anuncio.setUsuAnuncio(usuario);
        em.persist(anuncio);
        em.flush(); // Sincronizar el contexto de persistencia

        em.getTransaction().commit();

        // Usar el servicio para agregar el anuncio a favoritos
        boolean resultado = usuarioService.agregarFavorito(usuario, anuncio);

        assertTrue(resultado, "El anuncio debería agregarse exitosamente a favoritos.");

        em.getTransaction().begin();
        Usuario usuarioActualizado = em.find(Usuario.class, usuario.getIdUsuario());
        em.refresh(usuarioActualizado); // Asegurar que Hibernate recargue los datos desde la base
        assertTrue(usuarioActualizado.getFavoritos().contains(anuncio), "El anuncio debería estar en los favoritos del usuario.");
        em.getTransaction().commit();
    }

    @Test
    public void agregar_anuncio_a_favoritos_con_usuario_nulo_deberia_lanzar_excepcion() {
        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo("Anuncio de prueba");
        anuncio.setDescripcion("Descripción del anuncio de prueba");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.agregarFavorito(null, anuncio);
        });

        assertEquals("Usuario o anuncio no pueden ser nulos", exception.getMessage());
    }

    @Test
    public void agregar_anuncio_a_favoritos_con_anuncio_nulo_deberia_lanzar_excepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.agregarFavorito(usuario, null);
        });

        assertEquals("Usuario o anuncio no pueden ser nulos", exception.getMessage());
    }

    @Test
    public void agregar_anuncio_duplicado_a_favoritos_deberia_retornar_falso() throws NonexistentEntityException {
        em.getTransaction().begin();

        // Crear y persistir un anuncio
        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo("Anuncio duplicado");
        anuncio.setDescripcion("Descripción del anuncio duplicado");
        anuncio.setCategoria("Duplicado");
        anuncio.setPrecio(BigDecimal.valueOf(50.00));
        anuncio.setUsuAnuncio(usuario);
        em.persist(anuncio);
        em.flush();

        em.getTransaction().commit();

        // Agregar el anuncio a favoritos por primera vez
        boolean resultadoPrimero = usuarioService.agregarFavorito(usuario, anuncio);
        assertTrue(resultadoPrimero, "El anuncio debería agregarse exitosamente a favoritos la primera vez.");

        // Intentar agregar el mismo anuncio nuevamente
        boolean resultadoSegundo = usuarioService.agregarFavorito(usuario, anuncio);
        assertFalse(resultadoSegundo, "El anuncio no debería agregarse nuevamente a favoritos.");
    }

    @Test
    public void eliminar_anuncio_de_favoritos_exitosamente_usando_servicio() throws NonexistentEntityException {
        em.getTransaction().begin();

        // Crear y persistir un anuncio
        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo("Anuncio para eliminar");
        anuncio.setDescripcion("Descripción del anuncio para eliminar");
        anuncio.setCategoria("Eliminar");
        anuncio.setPrecio(BigDecimal.valueOf(20.00));
        anuncio.setUsuAnuncio(usuario);
        em.persist(anuncio);

        // Agregar el anuncio a favoritos
        usuario.getFavoritos().add(anuncio);
        em.merge(usuario);
        em.flush(); // Sincronizar el contexto de persistencia

        em.getTransaction().commit();

        // Usar el servicio para eliminar el anuncio de favoritos
        usuarioService.eliminarFavorito(usuario, anuncio);

        em.getTransaction().begin();
        Usuario usuarioActualizado = em.find(Usuario.class, usuario.getIdUsuario());
        em.refresh(usuarioActualizado); // Recargar el estado desde la base de datos
        assertFalse(usuarioActualizado.getFavoritos().contains(anuncio), "El anuncio debería ser eliminado de los favoritos.");
        em.getTransaction().commit();
    }

    @Test
    public void eliminar_anuncio_no_existente_de_favoritos_no_deberia_hacer_nada() throws NonexistentEntityException {
        em.getTransaction().begin();

        // Crear y persistir un anuncio no relacionado con favoritos
        Anuncio anuncioNoRelacionado = new Anuncio();
        anuncioNoRelacionado.setTitulo("Anuncio no favorito");
        anuncioNoRelacionado.setDescripcion("Descripción del anuncio no favorito");
        anuncioNoRelacionado.setCategoria("Prueba");
        anuncioNoRelacionado.setPrecio(BigDecimal.valueOf(20.00));
        anuncioNoRelacionado.setUsuAnuncio(usuario);
        em.persist(anuncioNoRelacionado);

        em.getTransaction().commit();

        // Intentar eliminar el anuncio no relacionado de favoritos
        usuarioService.eliminarFavorito(usuario, anuncioNoRelacionado);

        // Verificar que los favoritos no hayan cambiado
        em.getTransaction().begin();
        Usuario usuarioActualizado = em.find(Usuario.class, usuario.getIdUsuario());
        em.refresh(usuarioActualizado);
        assertFalse(usuarioActualizado.getFavoritos().contains(anuncioNoRelacionado), "El anuncio no relacionado no debería estar en los favoritos.");
        em.getTransaction().commit();
    }


    @Test
    public void ver_favoritos_del_usuario_usando_servicio() throws NonexistentEntityException {
        em.getTransaction().begin();

        // Crear y persistir anuncios
        Anuncio anuncio1 = new Anuncio();
        anuncio1.setTitulo("Anuncio 1");
        anuncio1.setDescripcion("Descripción del anuncio 1");
        anuncio1.setCategoria("Categoría 1");
        anuncio1.setPrecio(BigDecimal.valueOf(50.00));
        anuncio1.setUsuAnuncio(usuario);
        em.persist(anuncio1);

        Anuncio anuncio2 = new Anuncio();
        anuncio2.setTitulo("Anuncio 2");
        anuncio2.setDescripcion("Descripción del anuncio 2");
        anuncio2.setCategoria("Categoría 2");
        anuncio2.setPrecio(BigDecimal.valueOf(100.00));
        anuncio2.setUsuAnuncio(usuario);
        em.persist(anuncio2);

        // Agregar los anuncios a favoritos
        usuario.getFavoritos().add(anuncio1);
        usuario.getFavoritos().add(anuncio2);
        em.merge(usuario);
        em.flush(); // Sincronizar el contexto de persistencia

        em.getTransaction().commit();
        // Usar el servicio para ver los favoritos
        List<Anuncio> favoritos = usuarioService.verFavoritos(usuario);

        assertNotNull(favoritos, "La lista de favoritos no debería ser nula.");
        assertEquals(2, favoritos.size(), "Deberían haber 2 anuncios en la lista de favoritos.");
        assertTrue(favoritos.contains(anuncio1), "El anuncio 1 debería estar en los favoritos.");
        assertTrue(favoritos.contains(anuncio2), "El anuncio 2 debería estar en los favoritos.");
    }

    @Test
    public void ver_favoritos_de_usuario_sin_favoritos_deberia_retornar_lista_vacia() {
        List<Anuncio> favoritos = usuarioService.verFavoritos(usuario);

        assertNotNull(favoritos, "La lista de favoritos no debería ser nula.");
        assertTrue(favoritos.isEmpty(), "La lista de favoritos debería estar vacía.");
    }

    //Test parametrizado
    @ParameterizedTest
    @CsvSource({
            "true, true, true",   // Usuario y anuncio válidos, debe agregar
            "true, false, false", // Usuario válido, anuncio nulo, debe fallar
            "false, true, false"  // Usuario nulo, anuncio válido, debe fallar
    })
    public void testAgregarFavoritoParametrizado(boolean usuarioValido, boolean anuncioValido, boolean esperado) throws NonexistentEntityException {
        em.getTransaction().begin();
        // Configuración condicional de usuario y anuncio
        Usuario usuarioTest = usuarioValido ? usuario : null;

        Anuncio anuncioTest = null;
        if (anuncioValido) {
            anuncioTest = new Anuncio();
            anuncioTest.setTitulo("Anuncio de prueba");
            anuncioTest.setDescripcion("Descripción del anuncio de prueba");
            anuncioTest.setCategoria("Categoría de prueba");
            anuncioTest.setPrecio(BigDecimal.valueOf(50.00));
            anuncioTest.setUsuAnuncio(usuario);
            em.persist(anuncioTest);
            em.flush(); // Asegurar persistencia en la base
        }
        em.getTransaction().commit();
        // Llamar al método y capturar el resultado
        boolean resultado;
        try {
            resultado = usuarioService.agregarFavorito(usuarioTest, anuncioTest);
        } catch (IllegalArgumentException e) {
            resultado = false; // Capturamos las excepciones esperadas como un falso
        }

        // Validar el resultado
        assertEquals(esperado, resultado, "El resultado no coincide con el esperado para los parámetros proporcionados.");
    }


}
