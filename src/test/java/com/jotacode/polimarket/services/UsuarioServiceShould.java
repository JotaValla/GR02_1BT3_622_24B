package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.UsuarioDAO;
import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceShould {

    private UsuarioService usuarioService;
    private UsuarioDAO usuarioDAOMock;
    private ValoracionService valoracionServiceMock;

    @BeforeEach
    void setUp() {
        usuarioDAOMock = mock(UsuarioDAO.class);
        valoracionServiceMock = mock(ValoracionService.class);
        usuarioService = new UsuarioService();
        usuarioService.usuarioDAO = usuarioDAOMock;
        usuarioService.valoracionService = valoracionServiceMock;
    }

    // Pruebas de validación de nombres y correos
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

    // Pruebas de gestión de favoritos
    @Test
    public void agregar_anuncio_a_favoritos_exitosamente() throws NonexistentEntityException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setFavoritos(new ArrayList<>());

        Anuncio anuncio = new Anuncio();
        anuncio.setIdAnuncio(1L);

        when(usuarioDAOMock.findByIdWithFavoritos(usuario.getIdUsuario())).thenReturn(usuario);

        boolean resultado = usuarioService.agregarFavorito(usuario, anuncio);

        assertTrue(resultado, "El anuncio debería agregarse exitosamente a favoritos.");
        assertTrue(usuario.getFavoritos().contains(anuncio), "El anuncio debería estar en la lista de favoritos del usuario.");
        verify(usuarioDAOMock, times(1)).edit(usuario);
    }

    @Test
    public void no_agregar_anuncio_a_favoritos_si_ya_existe() throws NonexistentEntityException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        Anuncio anuncio = new Anuncio();
        anuncio.setIdAnuncio(1L);
        usuario.setFavoritos(List.of(anuncio));

        when(usuarioDAOMock.findByIdWithFavoritos(usuario.getIdUsuario())).thenReturn(usuario);

        boolean resultado = usuarioService.agregarFavorito(usuario, anuncio);

        assertFalse(resultado, "El anuncio no debería agregarse a favoritos porque ya existe.");
        verify(usuarioDAOMock, never()).edit(usuario);
    }

    @Test
    public void eliminar_anuncio_de_favoritos_exitosamente() throws NonexistentEntityException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        Anuncio anuncio = new Anuncio();
        anuncio.setIdAnuncio(1L);
        usuario.setFavoritos(new ArrayList<>(List.of(anuncio)));

        when(usuarioDAOMock.findByIdWithFavoritos(usuario.getIdUsuario())).thenReturn(usuario);

        usuarioService.eliminarFavorito(usuario, anuncio);

        assertFalse(usuario.getFavoritos().contains(anuncio), "El anuncio debería ser eliminado de los favoritos.");
        verify(usuarioDAOMock, times(1)).edit(usuario);
    }

    @Test
    public void ver_lista_de_favoritos() {
        Usuario usuario = new Usuario();
        Anuncio anuncio1 = new Anuncio();
        anuncio1.setIdAnuncio(1L);
        Anuncio anuncio2 = new Anuncio();
        anuncio2.setIdAnuncio(2L);
        usuario.setFavoritos(List.of(anuncio1, anuncio2));

        List<Anuncio> favoritos = usuarioService.verFavoritos(usuario);

        assertEquals(2, favoritos.size(), "El usuario debería tener dos anuncios en favoritos.");
        assertTrue(favoritos.contains(anuncio1), "El anuncio1 debería estar en favoritos.");
        assertTrue(favoritos.contains(anuncio2), "El anuncio2 debería estar en favoritos.");
    }

    // Pruebas de valoraciones
    @Test
    public void ver_valoraciones_del_usuario() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        Valoracion valoracion1 = new Valoracion();
        Valoracion valoracion2 = new Valoracion();
        List<Valoracion> valoraciones = List.of(valoracion1, valoracion2);

        when(valoracionServiceMock.findValoracionesByUsuario(usuario.getIdUsuario())).thenReturn(valoraciones);

        List<Valoracion> resultado = valoracionServiceMock.findValoracionesByUsuario(usuario.getIdUsuario());

        assertEquals(2, resultado.size(), "El usuario debería tener dos valoraciones.");
        verify(valoracionServiceMock, times(1)).findValoracionesByUsuario(usuario.getIdUsuario());
    }

    // Pruebas parametrizadas
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

    @ParameterizedTest
    @MethodSource("parameters")
    public void validar_si_el_nombre_es_valido(String username, boolean resultadoEsperado) {
        assertEquals(resultadoEsperado, usuarioService.validarNombre(username));
    }

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

    @ParameterizedTest
    @MethodSource("parameters2")
    public void validar_si_el_correo_es_valido(String email, boolean resultadoEsperado) {
        assertEquals(resultadoEsperado, usuarioService.validarEmail(email));
    }
}
