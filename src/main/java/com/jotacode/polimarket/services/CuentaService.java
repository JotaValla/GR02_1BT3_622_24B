package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.CuentaDAO;
import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.models.entity.Cuenta;

public class CuentaService {

    public CuentaDAO cuentaDAO;

    public CuentaService() {
        this.cuentaDAO = new CuentaDAO(null, Cuenta.class);
    }

    public Cuenta crearCuenta(String username, String password) {
        Cuenta cuenta = new Cuenta();
        validarCamposCuenta(username, password);
        cuenta.setUsername(username);
        cuenta.setPassword(password);
        cuentaDAO.create(cuenta);
        System.out.println("Cuenta creada en servicio");
        return cuenta;
    }

    private static void validarCamposCuenta(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Todo los campos deben ser llenados");
        }
    }


    private static void validarCuenta(Cuenta cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula");
        }
    }


    public Cuenta findByUsernameAndPassword(String username, String password) {
        return cuentaDAO.findByUsernameAndPassword(username, password);
    }

    public boolean validatePassword(Cuenta cuenta, String currentPassword) {
        return cuenta.getPassword().equals(currentPassword);
    }

    public void updatePassword(Cuenta cuenta, String newPassword) {
        try {
            cuenta.setPassword(newPassword);
            cuentaDAO.edit(cuenta);
        } catch (NonexistentEntityException e) {
            throw new RuntimeException(e);
        }
    }
}
