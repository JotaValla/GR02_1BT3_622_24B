package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.CuentaDAO;
import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.models.entity.Cuenta;
import jakarta.persistence.NoResultException;

public class CuentaService {

    public CuentaDAO cuentaDAO;

    public CuentaService() {
        this.cuentaDAO = new CuentaDAO(null, Cuenta.class);
    }

    public boolean existsUsername(String username) {
        return cuentaDAO.existsUsername(username.trim().toLowerCase());
    }

    public Cuenta crearCuenta(String username, String password) {
        validarCamposCuenta(username, password);
        validarFormatoUsername(username);
        
        if (existsUsername(username)) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        Cuenta cuenta = new Cuenta();
        cuenta.setUsername(username.trim().toLowerCase());
        cuenta.setPassword(password);
        cuentaDAO.create(cuenta);
        return cuenta;
    }

    private static void validarCamposCuenta(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Todos los campos deben ser llenados");
        }
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario y la contraseña no pueden estar vacíos");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("El nombre de usuario debe tener minimo 3 caracteres");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres, una mayúscula y un número" + password);
        }
    }

    private static void validarFormatoUsername(String username) {
        if (!username.matches("^[a-zA-Z0-9]{3,}$")) {
            throw new IllegalArgumentException("El nombre de usuario debe contener solo caracteres alfanuméricos");
        }
    }

    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{6,16}$";
        return password.matches(passwordRegex);
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
