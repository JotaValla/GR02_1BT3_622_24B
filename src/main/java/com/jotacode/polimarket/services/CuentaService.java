package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.CuentaDAO;
import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.models.entity.Cuenta;
import jakarta.persistence.NoResultException;

/**
 * Servicio que gestiona las operaciones relacionadas con las cuentas de usuario.
 * Maneja la creación, validación y actualización de cuentas, incluyendo la gestión
 * de nombres de usuario y contraseñas.
 */
public class CuentaService {

    public CuentaDAO cuentaDAO;

    public CuentaService() {
        this.cuentaDAO = new CuentaDAO(null, Cuenta.class);
    }

    /**
     * Verifica si un nombre de usuario ya existe en el sistema.
     * @param username Nombre de usuario a verificar
     * @return true si el username ya existe, false en caso contrario
     */
    public boolean existsUsername(String username) {
        return cuentaDAO.existsUsername(username.trim().toLowerCase());
    }

    /**
     * Valida que una contraseña cumpla con los requisitos de seguridad.
     * La contraseña debe contener:
     * - Entre 8 y 16 caracteres
     * - Al menos una letra mayúscula
     * - Al menos una letra minúscula
     * - Al menos un número
     * - Al menos un carácter especial
     *
     * @param password Contraseña a validar
     * @return true si la contraseña cumple con todos los requisitos
     */
    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_])[A-Za-z\\d\\W_]{8,16}$";
        return password.matches(passwordRegex);
    }

    public boolean validarFormatoUsername(String username) {
        String usernameRegex = "^[a-zA-Z0-9]{3,15}$";
        return username != null && username.matches(usernameRegex);
    }

    public Cuenta crearCuenta(String username, String password) {
        validarCamposCuenta(username, password);
        if (existsUsername(username)) {
            throw new IllegalArgumentException("El nombre de usuario ya existe.");
        }

        Cuenta cuenta = new Cuenta();
        cuenta.setUsername(username.trim().toLowerCase());
        cuenta.setPassword(password);
        cuentaDAO.create(cuenta);
        return cuenta;
    }

    private void validarCamposCuenta(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener entre 8 y 16 caracteres, al menos una mayúscula, una minúscula, un número y un carácter especial.");
        }
    }

    private static void validarCuenta(Cuenta cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula");
        }
    }

    public boolean validatePassword(Cuenta cuenta, String currentPassword) {
        validarCuenta(cuenta);
        return cuenta.getPassword().equals(currentPassword);
    }

    public void updatePassword(Cuenta cuenta, String newPassword) {
        validarCuenta(cuenta);
        if (newPassword.equals(cuenta.getPassword())) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser igual a la anterior");
        }
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("La nueva contraseña no cumple con los requisitos de complejidad");
        }
        try {
            cuenta.setPassword(newPassword);
            cuentaDAO.edit(cuenta);
        } catch (NonexistentEntityException e) {
            throw new RuntimeException(e);
        }
    }

    public Cuenta findByUsernameAndPassword(String username, String password) {
        try {
            return cuentaDAO.findByUsernameAndPassword(username.trim().toLowerCase(), password);
        } catch (NoResultException e) {
            return null;
        }
    }

}
