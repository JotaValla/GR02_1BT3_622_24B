package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.UsuarioDAO;
import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Cuenta;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;

import java.util.List;

/**
 * Servicio que gestiona las operaciones relacionadas con los usuarios del sistema.
 * Maneja el registro, actualización y gestión de usuarios, incluyendo sus anuncios,
 * valoraciones y lista de favoritos.
 */
public class UsuarioService {

    public UsuarioDAO usuarioDAO;
    private AnuncioService anuncioService;
    public ValoracionService valoracionService;
    public CuentaService cuentaService;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO(null, Usuario.class);
        this.anuncioService = new AnuncioService();
        this.valoracionService = new ValoracionService();
        this.cuentaService = new CuentaService();
    }

    public void validarDatosRegistro(String usernameCuenta, String password, String nombre, String email) {
        if (!cuentaService.isValidPassword(password)) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener entre 8 y 16 caracteres, al menos una mayúscula, una minúscula, un número y un carácter especial.");
        }

        if (!validarNombre(nombre)) {
            throw new IllegalArgumentException(
                    "El nombre debe tener mínimo 3 caracteres y solo contener letras, tildes y espacios.");
        }

        if (!cuentaService.validarFormatoUsername(usernameCuenta)) {
            throw new IllegalArgumentException(
                    "El nombre de usuario debe tener entre 3 y 15 caracteres. Solo puede contener letras y números.");
        }

        if (!validarEmail(email)) {
            throw new IllegalArgumentException("El email ingresado no tiene un formato válido.");
        }

        if (cuentaService.existsUsername(usernameCuenta)) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }
    }

    public void crearUsuarioConCuenta(String usernameCuenta, String password, String nombre, String telefono, String email) {
        Cuenta cuenta = cuentaService.crearCuenta(usernameCuenta, password);
        crearUsuario(nombre, telefono, email, cuenta);
    }

    public void crearUsuario(String nombre, String telefono, String email, Cuenta cuenta) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuario.setCuenta(cuenta);

        usuarioDAO.create(usuario);
    }

    /**
     * Valida el formato de un correo electrónico.
     * @param email Correo a validar
     * @return true si el formato es válido
     */
    public boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(regex);
    }

    public boolean validarNombre(String nombre) {
        String regex = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{3,}$";
        return nombre != null && nombre.matches(regex);
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

    public boolean agregarFavorito(Usuario usuario, Anuncio anuncio) throws NonexistentEntityException {
        if (usuario == null || anuncio == null) {
            throw new IllegalArgumentException("Usuario o anuncio no pueden ser nulos");
        }
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

    public boolean updateUserInfo(Usuario usuario, String newPhone, String newEmail) {
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

        // Solo realizar la actualización en la base de datos si hubo cambios
        if (isUpdated) {
            try {
                usuarioDAO.edit(usuario); // Guardar la información actualizada
            } catch (NonexistentEntityException e) {
                throw new RuntimeException("Error al actualizar la información del usuario.", e);
            }
        }

        return isUpdated;
    }

}
