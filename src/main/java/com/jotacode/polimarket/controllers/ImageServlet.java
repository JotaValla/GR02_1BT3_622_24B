package com.jotacode.polimarket.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/uploads/*")
public class ImageServlet extends HttpServlet {
    private static final String UPLOAD_DIRECTORY = "C:\\Users\\djimm\\OneDrive - Escuela Politécnica Nacional\\VISEMESTREV2.0\\METODOLOGIAS\\PoliMarket\\uploads\\anuncios";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filename = request.getPathInfo().substring(1); // Obtiene el nombre del archivo
        File file = new File(UPLOAD_DIRECTORY, filename);

        if (file.exists() && !file.isDirectory()) {
            response.setHeader("Content-Type", getServletContext().getMimeType(filename));
            response.setHeader("Content-Length", String.valueOf(file.length()));
            Files.copy(file.toPath(), response.getOutputStream());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // Imagen no encontrada
        }
    }
}
