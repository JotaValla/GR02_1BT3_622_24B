package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.UsuarioDAO;
import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Cuenta;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;

import java.util.List;

public class UsuarioService {

    public UsuarioDAO usuarioDAO;
    private AnuncioService anuncioService;
    public ValoracionService valoracionService;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO(null, Usuario.class);
        this.anuncioService = new AnuncioService();
        this.valoracionService = new ValoracionService();
    }

    public void crearUsuario(String nombre, String telefono, String email, Cuenta cuenta) {
        if (!validarEmail(email)) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
        if (!validarNombre(nombre)) {
            throw new IllegalArgumentException("El formato del nombre no es válido");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuario.setCuenta(cuenta);

        usuarioDAO.create(usuario);
    }

    public boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Expresión regular mejorada para email
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }


    public boolean validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        // Expresión regular corregida para nombres con espacios y tildes
        String regex = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{3,}$";
        return nombre.matches(regex);
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

    public boolean agregarFavorito(Usuario usuario, Anuncio anuncio) throws NonexistentEntityException {
        Usuario usuarioConFavoritos = usuarioDAO.findByIdWithFavoritos(usuario.getIdUsuario());

        // Verifica que el anuncio no esté ya en favoritos para evitar duplicados
        if (usuarioConFavoritos.getFavoritos().stream().noneMatch(f -> f.getIdAnuncio().equals(anuncio.getIdAnuncio()))) {
            usuarioConFavoritos.getFavoritos().add(anuncio);
            usuarioDAO.edit(usuarioConFavoritos);
            return true; // Anuncio agregado exitosamente
        }
        return false; // El anuncio ya estaba en favoritos
    }
    public void eliminarFavorito(Usuario usuario, Anuncio anuncio) throws NonexistentEntityException {
        // Recarga el usuario con la colección de favoritos inicializada
        Usuario usuarioConFavoritos = usuarioDAO.findByIdWithFavoritos(usuario.getIdUsuario());

        if (usuarioConFavoritos.getFavoritos().contains(anuncio)) {
            usuarioConFavoritos.getFavoritos().remove(anuncio);
            usuarioDAO.edit(usuarioConFavoritos);  // Guarda los cambios en la base de datos
        }
    }

    public List<Anuncio> verFavoritos(Usuario usuario) {
        return usuario.getFavoritos();
    }

    public Usuario findById(Long id) {
        return usuarioDAO.findByIdWithFavoritos(id); // Nuevo método que carga favoritos de forma anticipada
    }

    public Usuario findUsuarioConAnuncios(Cuenta cuenta) {
        Usuario usuario = usuarioDAO.findByCuenta(cuenta); // Primero obtienes el usuario por su cuenta
        return usuarioDAO.findByIdWithAnuncios(usuario.getIdUsuario()); // Luego lo recargas con los anuncios
    }
    public void updateUserInfo(Usuario usuario, String newPhone, String newEmail) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }

        boolean isUpdated = false;

        // Validar y actualizar teléfono
        if (newPhone != null && !newPhone.equals(usuario.getTelefono())) {
            usuario.setTelefono(newPhone);
            isUpdated = true;
        }

        // Validar y actualizar email
        if (newEmail != null && !newEmail.equals(usuario.getEmail())) {
            if (!validarEmail(newEmail)) {
                throw new IllegalArgumentException("El formato del email no es válido");
            }
            usuario.setEmail(newEmail);
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new IllegalArgumentException("No se realizaron cambios en la información del usuario.");
        }

        try {
            usuarioDAO.edit(usuario); // Guardar la información actualizada
        } catch (NonexistentEntityException e) {
            throw new RuntimeException("Error al actualizar la información del usuario.", e);
        }
    }
}
