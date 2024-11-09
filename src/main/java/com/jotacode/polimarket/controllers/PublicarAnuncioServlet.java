package com.jotacode.polimarket.controllers;

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
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;

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
        maxRequestSize = 1024 * 1024 * 10    // 10 MB
)
public class PublicarAnuncioServlet extends HttpServlet {

    private static final String DEFAULT_IMAGE_URL = "/uploads/anuncios/defaultAnuncio.jpg"; // URL para la imagen por defecto
    private AnuncioService anuncioService = new AnuncioService();
    private UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/publicarAnuncio.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Anuncio anuncio = createAnuncioFromRequest(request, request.getPart("imagen"));
        usuarioService.publicarAnuncio(anuncio, usuario);

        // Redirige con un mensaje de éxito
        response.sendRedirect(request.getContextPath() + "/verAnuncios?status=success");
    }

    public Anuncio createAnuncioFromRequest(HttpServletRequest request, Part imagenPart) throws IOException {
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String categoria = request.getParameter("categoria");
        BigDecimal precio = new BigDecimal(request.getParameter("precio"));

        String uploadDirectory = request.getServletContext().getRealPath("/uploads/anuncios");
        String imagenReferencia;

        // Verifica si se ha subido una imagen
        if (imagenPart != null && imagenPart.getSize() > 0) {
            // Genera un nombre único para la imagen
            String imagenNombre = UUID.randomUUID().toString() + "_" + imagenPart.getSubmittedFileName();

            // Crea el directorio si no existe
            File uploadDir = new File(uploadDirectory);
            if (!uploadDir.exists()) {
                Files.createDirectories(Paths.get(uploadDirectory));
            }

            // Guarda la imagen en el directorio
            String uploadPath = uploadDirectory + File.separator + imagenNombre;
            imagenPart.write(uploadPath);

            // Guarda la referencia de la imagen para almacenar en la base de datos
            imagenReferencia = request.getContextPath() + "/uploads/anuncios/" + imagenNombre;
        } else {
            // Asigna la URL de la imagen por defecto que está en la carpeta webapp/uploads/anuncios
            imagenReferencia = request.getContextPath() + DEFAULT_IMAGE_URL;
        }

        // Crea el anuncio con la imagen cargada o la imagen por defecto
        return anuncioService.crearAnuncio(titulo, descripcion, imagenReferencia, categoria, precio);
    }
}

