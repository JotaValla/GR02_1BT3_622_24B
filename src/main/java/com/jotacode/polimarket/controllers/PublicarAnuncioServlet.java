package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.services.AnuncioService;
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
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class PublicarAnuncioServlet extends HttpServlet {

    private static final String UPLOAD_DIRECTORY = "C:\\Users\\djimm\\OneDrive - Escuela Politécnica Nacional\\VISEMESTREV2.0\\METODOLOGIAS\\PoliMarket\\uploads\\anuncios";
    private AnuncioService anuncioService = new AnuncioService();

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
        anuncioService.vincularAnuncioConUsuario(anuncio, usuario);

        response.sendRedirect(request.getContextPath() + "/verAnuncios");
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
