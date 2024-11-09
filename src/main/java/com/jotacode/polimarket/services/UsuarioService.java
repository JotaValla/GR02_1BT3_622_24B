package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.UsuarioDAO;
import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Cuenta;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;

import java.util.List;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;
    private AnuncioService anuncioService;
    private ValoracionService valoracionService;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO(null, Usuario.class);
        this.anuncioService = new AnuncioService();
        this.valoracionService = new ValoracionService();
    }

    public void crearUsuario(String nombre, String foto, String telefono, String email, Cuenta cuenta) {
        if (validarEmail(email) && validarNombre(nombre)) {
            validarEmail(email);
            validarNombre(nombre);

            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setFoto(foto);
            usuario.setTelefono(telefono);
            usuario.setEmail(email);
            usuario.setCuenta(cuenta);

            usuarioDAO.create(usuario);
            System.out.println("Usuario creado en servicio");
        } else {
            throw new IllegalArgumentException("El email o el nombre proporcionado no es válido.");
        }

    }

    public boolean validarEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"; // Un regex simple para validación
        if (!email.matches(regex)) {
            return false;
        }
        return true;
    }

    public boolean validarNombre(String nombre) {
        String regex = "^[a-zA-Z0-9]{3,}$"; // Solo letras y números, longitud mínima de 3 caracteres
        if (nombre == null || !nombre.matches(regex)) {
            return false;
        }
        return true;
    }

    public void publicarAnuncio(Anuncio anuncio, Usuario usuario) {
        anuncioService.vincularAnuncioConUsuario(anuncio, usuario);
    }

    public void publicarValoracion(Valoracion valoracion, Anuncio anuncio, Usuario usuario) {
        valoracionService.vincularValoracion(valoracion, anuncio, usuario);
    }

    public List<Anuncio> verAnuncios() {
        return anuncioService.findAllAnuncios();
    }

    public List<Usuario> findAllUsuarios() {
        return usuarioDAO.findAll();
    }

    public Usuario findByCuenta(Cuenta cuenta) {
        return usuarioDAO.findByCuenta(cuenta);
    }

    public void agregarFavorito(Usuario usuario, Anuncio anuncio) throws NonexistentEntityException {
        // Recarga el usuario desde la base de datos para obtener la colección favoritos inicializada
        Usuario usuarioConFavoritos = usuarioDAO.findByIdWithFavoritos(usuario.getIdUsuario());

        if (!usuarioConFavoritos.getFavoritos().contains(anuncio)) {
            usuarioConFavoritos.getFavoritos().add(anuncio);
            usuarioDAO.edit(usuarioConFavoritos); // Guarda los cambios en la base de datos
        }
    }


    public List<Anuncio> verFavoritos(Usuario usuario) {
        return usuario.getFavoritos();
    }

    public Usuario findById(Long id) {
        return usuarioDAO.findByIdWithFavoritos(id); // Nuevo método que carga favoritos de forma anticipada
    }

}
