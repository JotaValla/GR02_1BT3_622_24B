package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.AnuncioDAO;
import com.jotacode.polimarket.models.dao.UsuarioDAO;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;
    private AnuncioService anuncioService;
    private ValoracionService valoracionService;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO(null, Usuario.class);
        this.anuncioService = new AnuncioService();
        this.valoracionService = new ValoracionService();
    }

    public Usuario crearUsuario(String username, String foto, String telefono, String email) {
        // Validar el email antes de crear el usuario
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("El email proporcionado no es válido.");
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setFoto(foto);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuarioDAO.create(usuario);
        return usuario;
    }

    public boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"; // Un regex simple para validación
        return email.matches(regex);
    }

    public void publicarAnuncio(Anuncio anuncio, Usuario usuario) {
        anuncioService.vincularAnuncioConUsuario(anuncio, usuario);
        System.out.println("Anuncio publicado");
    }

    public void publicarValoracion(Valoracion valoracion, Anuncio anuncio,Usuario usuario){
        valoracionService.vincularValoracion(valoracion, anuncio,usuario);
        System.out.println("Valoracion publicada");
    }

    public List<Anuncio> verAnuncios(){
        return anuncioService.findAllAnuncios();
    }

    public List<Usuario> findAllUsuarios() {
        return usuarioDAO.findAll();
    }
}
