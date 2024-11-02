package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.CuentaDAO;
import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.models.entity.Cuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

public class CuentaServiceShould {

    private CuentaService cuentaService;
    private CuentaDAO cuentaDAOMock;

    @BeforeEach
    public void setUp() {
        cuentaDAOMock = mock(CuentaDAO.class);
        cuentaService = new CuentaService();
        cuentaService.cuentaDAO = cuentaDAOMock;
    }

    //Pruebas con mockito
    @Test
    public void crear_cuenta_llamando_al_metodo_create_del_dao() {
        // Llama al método para crear cuenta, usa el nombre en mayúsculas como entrada
        cuentaService.crearCuenta("UsuarioDePrueba", "Password1");

        // Crea el objeto cuenta con el nombre en minúsculas, tal y como lo hace el servicio
        Cuenta cuentaEsperada = new Cuenta();
        cuentaEsperada.setUsername("usuariodeprueba");
        cuentaEsperada.setPassword("Password1");

        // Verifica que el método create del DAO fue llamado con el objeto correctamente formateado
        verify(cuentaDAOMock, times(1)).create(eq(cuentaEsperada));
    }

    @Test
    public void encontrar_cuenta_por_usuario_y_contrasena() {
        // Mockear respuesta esperada del DAO
        Cuenta cuenta = new Cuenta();
        cuenta.setUsername("usuarioprueba");
        cuenta.setPassword("Password1");
        when(cuentaDAOMock.findByUsernameAndPassword("usuarioprueba", "Password1")).thenReturn(cuenta);

        // Llamar al método
        Cuenta cuentaEncontrada = cuentaService.findByUsernameAndPassword("usuarioPrueba", "Password1");

        // Verificar que el método fue llamado y que la cuenta encontrada es correcta
        verify(cuentaDAOMock, times(1)).findByUsernameAndPassword("usuarioprueba", "Password1");
        assertEquals("usuarioprueba", cuentaEncontrada.getUsername());
        assertEquals("Password1", cuentaEncontrada.getPassword());
    }

    @Test
    public void actualizar_contrasena_llamando_al_metodo_edit_del_dao() throws NonexistentEntityException {
        // Crear una cuenta y establecer una nueva contraseña
        Cuenta cuenta = new Cuenta();
        cuenta.setPassword("Password1");

        // Llamar al método de actualización
        cuentaService.updatePassword(cuenta, "NuevaPassword1");

        // Verificar que el método edit del DAO fue llamado
        verify(cuentaDAOMock, times(1)).edit(cuenta);
    }

    @Test
    public void lanzar_excepcion_al_actualizar_contrasena_si_la_cuenta_no_existe() throws NonexistentEntityException {
        // Crear una cuenta y simular una excepción en el DAO
        Cuenta cuenta = new Cuenta();
        cuenta.setPassword("Password1");
        doThrow(new NonexistentEntityException("La cuenta no existe")).when(cuentaDAOMock).edit(cuenta);

        // Verificar que se lanza una excepción al intentar actualizar la contraseña de una cuenta inexistente
        assertThrows(RuntimeException.class, () -> cuentaService.updatePassword(cuenta, "NuevaPassword1"));
    }


    //Pruebas unitarias
    @Test
    public void lanzar_excepcion_si_username_es_demasiado_corto() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                cuentaService.crearCuenta("us", "Password1"));
        assertEquals("El nombre de usuario debe tener entre 3 y 15 caracteres", exception.getMessage());
    }

    @Test
    public void lanzar_excepcion_si_username_es_demasiado_largo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                cuentaService.crearCuenta("username_muy_largo_para_prueba", "Password1"));
        assertEquals("El nombre de usuario debe tener entre 3 y 15 caracteres", exception.getMessage());
    }

    @Test
    public void lanzar_excepcion_si_username_tiene_caracteres_no_alfanumericos() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                cuentaService.crearCuenta("user$name", "Password1"));
        assertEquals("El nombre de usuario debe contener solo caracteres alfanuméricos", exception.getMessage());
    }

    @Test
    public void validar_contrasena_correcta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setPassword("Password1");
        assertTrue(cuentaService.validatePassword(cuenta, "Password1"));
    }

    @Test
    public void no_validar_contrasena_incorrecta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setPassword("Password1");
        assertFalse(cuentaService.validatePassword(cuenta, "Password2"));
    }

    @Test
    public void lanzar_excepcion_si_la_cuenta_es_nula_al_validar_contrasena() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                cuentaService.validatePassword(null, "Password1"));
        assertEquals("La cuenta no puede ser nula", exception.getMessage());
    }

    @Test
    public void lanzar_excepcion_si_nueva_contrasena_es_igual_a_la_anterior() {
        Cuenta cuenta = new Cuenta();
        cuenta.setPassword("Password1");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                cuentaService.updatePassword(cuenta, "Password1"));
        assertEquals("La nueva contraseña no puede ser igual a la anterior", exception.getMessage());
    }

    @Test
    public void lanzar_excepcion_si_nueva_contrasena_no_cumple_requisitos() {
        Cuenta cuenta = new Cuenta();
        cuenta.setPassword("Password1");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                cuentaService.updatePassword(cuenta, "pass"));
        assertEquals("La nueva contraseña no cumple con los requisitos de complejidad", exception.getMessage());
    }

    //Pruebas parametrizadas
    @ParameterizedTest
    @ValueSource(strings = {"Password1", "MySecurePass2", "AnotherPass3"})
    public void validar_contrasena_correcta_con_parametros(String password) {
        Cuenta cuenta = new Cuenta();
        cuenta.setPassword(password);

        assertTrue(cuentaService.validatePassword(cuenta, password));
    }

    @ParameterizedTest
    @CsvSource({
            "user1, pass",       // Contraseña demasiado corta
            "user2, password",   // Sin mayúsculas ni números
            "user3, PASSWORD"    // Sin minúsculas ni números
    })
    public void lanzar_excepcion_si_contrasena_no_cumple_requisitos(String username, String password) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                cuentaService.crearCuenta(username, password)
        );
        assertEquals("La contraseña debe tener al menos 6 caracteres, una mayúscula y un número", exception.getMessage());
    }



}