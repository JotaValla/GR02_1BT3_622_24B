package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.dao.AnuncioDAO;
import com.jotacode.polimarket.models.dao.UsuarioDAO;
import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.services.AnuncioService;
import com.jotacode.polimarket.services.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@WebServlet("/publicarAnuncio")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class PublicarAnuncioServlet extends HttpServlet {

    // Definir la ruta fuera de `target` para mantener los archivos a salvo en recompilaciones
    private static final String UPLOAD_DIRECTORY = "C:\\Users\\djimm\\OneDrive - Escuela Politécnica Nacional\\VISEMESTREV2.0\\METODOLOGIAS\\PoliMarket\\uploads\\anuncios";
    private UsuarioService usuarioService;
    private AnuncioService anuncioService;

    public PublicarAnuncioServlet() {
        this.usuarioService = new UsuarioService();
        this.anuncioService = new AnuncioService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/publicarAnuncio.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Usuario usuario = createUserFromRequest(request);
        Anuncio anuncio = createAnuncioFromRequest(request, request.getPart("imagen"));

        usuarioService.publicarAnuncio(anuncio, usuario);

        response.sendRedirect(request.getContextPath() + "/verAnuncios");
    }

    private Usuario createUserFromRequest(HttpServletRequest request) {
        String username = request.getParameter("username");
        String foto = request.getParameter("foto");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        return usuarioService.crearUsuario(username, foto, telefono, email);
    }

    private Anuncio createAnuncioFromRequest(HttpServletRequest request, Part imagenPart) throws IOException {
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String categoria = request.getParameter("categoria");
        BigDecimal precio = new BigDecimal(request.getParameter("precio"));

        // Generar un nombre único para la imagen
        String imagenNombre = UUID.randomUUID().toString() + "_" + imagenPart.getSubmittedFileName();

        // Verificar si el directorio de almacenamiento existe y crearlo si no
        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            Files.createDirectories(Paths.get(UPLOAD_DIRECTORY));
        }

        // Guardar la imagen en el directorio
        String uploadPath = UPLOAD_DIRECTORY + File.separator + imagenNombre;
        imagenPart.write(uploadPath);

        // Guardar solo la referencia de la imagen para almacenar en la base de datos
        String imagenReferencia = "/uploads/anuncios/" + imagenNombre;

        // Crear el anuncio
        return anuncioService.crearAnuncio(titulo, descripcion, imagenReferencia, categoria, precio);
    }

}