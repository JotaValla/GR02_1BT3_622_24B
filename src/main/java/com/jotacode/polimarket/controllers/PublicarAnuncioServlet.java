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

    private static final String UPLOAD_DIRECTORY = "C:\\Users\\djimm\\OneDrive - Escuela Politécnica Nacional\\VISEMESTREV2.0\\METODOLOGIAS\\PoliMarket\\uploads\\anuncios";
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

        String titulo = request.getParameter("titulo");
        if (!validarTitulo(titulo)) {
            request.setAttribute("errorMessage", "El título solo puede contener letras, números, espacios, comas, puntos y tildes");
            request.getRequestDispatcher("/WEB-INF/views/publicarAnuncio.jsp").forward(request, response);
            return;
        }

        try {
            Anuncio anuncio = createAnuncioFromRequest(request, request.getPart("imagen"));
            usuarioService.publicarAnuncio(anuncio, usuario);
            response.sendRedirect(request.getContextPath() + "/verAnuncios?status=success");
        } catch (Exception e) {
            // Maneja el error y lo muestra en la misma página
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/publicarAnuncio.jsp").forward(request, response);
        }
    }



    private boolean validarTitulo(String titulo) {
        // Permite letras (incluyendo tildes), números, espacios, comas y puntos
        String tituloRegex = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\s,\\.]+$";
        return titulo != null && titulo.matches(tituloRegex);
    }

    public Anuncio createAnuncioFromRequest(HttpServletRequest request, Part imagenPart) throws IOException {
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String categoria = request.getParameter("categoria");
        BigDecimal precio = new BigDecimal(request.getParameter("precio"));

        String imagenReferencia;

        // Verifica si se ha subido una imagen
        if (imagenPart != null && imagenPart.getSize() > 0) {
            // Valida el tipo de contenido
            String contentType = imagenPart.getContentType();
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                throw new IOException("Solo se permiten archivos en formato JPG o PNG.");
            }

            // Valida el tamaño del archivo
            long fileSize = imagenPart.getSize();
            if (fileSize > 10 * 1024 * 1024) { // 10 MB en bytes
                throw new IOException("El archivo es demasiado grande. El tamaño máximo permitido es de 10 MB.");
            }

            // Genera un nombre único para la imagen
            String imagenNombre = UUID.randomUUID().toString() + "_" + imagenPart.getSubmittedFileName();

            // Crea el directorio si no existe
            File uploadDir = new File(UPLOAD_DIRECTORY);
            if (!uploadDir.exists()) {
                Files.createDirectories(Paths.get(UPLOAD_DIRECTORY));
            }

            // Guarda la imagen en el directorio especificado
            String uploadPath = UPLOAD_DIRECTORY + File.separator + imagenNombre;
            imagenPart.write(uploadPath);

            // Guarda solo la ruta relativa en la base de datos
            imagenReferencia = "/uploads/anuncios/" + imagenNombre;
        } else {
            // Asigna la URL de la imagen por defecto
            imagenReferencia = DEFAULT_IMAGE_URL;
        }

        // Crea el anuncio con la imagen cargada o la imagen por defecto
        return anuncioService.crearAnuncio(titulo, descripcion, imagenReferencia, categoria, precio);
    }




}
