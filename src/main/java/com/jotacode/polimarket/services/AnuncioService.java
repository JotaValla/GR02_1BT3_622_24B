package com.jotacode.polimarket.services;

import com.jotacode.polimarket.models.dao.AnuncioDAO;
import com.jotacode.polimarket.models.dao.exceptions.NonexistentEntityException;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;

import java.math.BigDecimal;
import java.util.List;

public class AnuncioService {

    public AnuncioDAO anuncioDAO;

    public AnuncioService() {
        this.anuncioDAO = new AnuncioDAO(null, Anuncio.class);
    }

    /**
     * Crea un nuevo anuncio con los datos proporcionados.
     * @param titulo Título del anuncio
     * @param descripcion Descripción detallada del anuncio
     * @param imagen URL o ruta de la imagen del anuncio
     * @param categoria Categoría a la que pertenece el anuncio
     * @param precio Precio del artículo o servicio anunciado
     * @return Objeto Anuncio creado
     */

    public Anuncio crearAnuncio(String titulo, String descripcion, String imagen, String categoria, BigDecimal precio){
        Anuncio anuncio = new Anuncio();
        validarCamposAnuncio(titulo, descripcion, imagen, categoria, precio);
        anuncio.setTitulo(titulo);
        anuncio.setDescripcion(descripcion);
        anuncio.setImagen(imagen);
        anuncio.setCategoria(categoria);
        anuncio.setPrecio(precio);
        return anuncio;
    }

    private static void validarCamposAnuncio(String titulo, String descripcion, String imagen, String categoria, BigDecimal precio) {
        if (titulo == null || descripcion == null || categoria == null || precio == null) {
            throw new IllegalArgumentException("Todos los campos deben ser llenados");
        }
        if (precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
    }

    /**
     * Vincula un anuncio con un usuario y lo persiste en la base de datos.
     * @param anuncio Anuncio a vincular
     * @param usuario Usuario que publica el anuncio
     */
    public void vincularAnuncioConUsuario(Anuncio anuncio, Usuario usuario) throws IllegalArgumentException {
        validarAnuncioyUsuario(anuncio, usuario);
        anuncio.setUsuAnuncio(usuario);
        anuncioDAO.create(anuncio);
    }

    private static void validarAnuncioyUsuario(Anuncio anuncio, Usuario usuario) {
        if (anuncio == null || usuario == null) {
            throw new IllegalArgumentException("El anuncio y el usuario no pueden ser nulos");
        }
    }

    public List<Anuncio> findAllAnuncios() {
        return anuncioDAO.findAll();
    }

    public List<Anuncio> findAnunciosByCategoria(String categoria) {
        return anuncioDAO.findAnunciosByCategoria(categoria);
    }

    public Anuncio findById(Long id) {
        return anuncioDAO.find(id);
    }

    public List<Anuncio> findAnunciosByUsuario(long idUsuario) {
        return anuncioDAO.findAnunciosByUsuario(idUsuario);
    }

    /**
     * Calcula el promedio de las valoraciones de un anuncio.
     * @param anuncioId ID del anuncio
     * @return Promedio de valoraciones o 0.0 si no hay valoraciones
     */
    public double calcularPromedioValoraciones(Long anuncioId) {
        Anuncio anuncio = anuncioDAO.findByIdWithValoraciones(anuncioId); // Cargar anuncio con valoraciones
        List<Valoracion> valoraciones = anuncio.getValoraciones();

        // Si no hay valoraciones, el promedio es 0.0
        return valoraciones.isEmpty() ? 0.0 :
                valoraciones.stream()
                        .mapToDouble(Valoracion::getEstrellas)
                        .average()
                        .orElse(0.0); // En caso improbable de error en el cálculo, retorna 0.0
    }

    public void actualizarAnuncio(Anuncio anuncio) throws NonexistentEntityException {
        anuncioDAO.edit(anuncio);
    }

    /**
     * Busca anuncios que coincidan con un usuario y categoría específicos.
     * @param usuarioId ID del usuario
     * @param categoria Categoría a filtrar
     * @return Lista de anuncios que cumplen ambos criterios
     */
    public List<Anuncio> findAnunciosByUsuarioAndCategoria(Long usuarioId, String categoria) {
        return anuncioDAO.findAnunciosByUsuarioAndCategoria(usuarioId, categoria);
    }
}
