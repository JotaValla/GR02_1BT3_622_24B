package com.jotacode.polimarket.controllers;

import com.jotacode.polimarket.models.entity.Anuncio;
import com.jotacode.polimarket.models.entity.Usuario;
import com.jotacode.polimarket.models.entity.Valoracion;
import com.jotacode.polimarket.services.AnuncioService;
import com.jotacode.polimarket.services.ValoracionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/verValoraciones")
public class VerValoracionesServlet extends HttpServlet {

    private ValoracionService valoracionService;
    private AnuncioService anuncioService;

    public VerValoracionesServlet() {
        this.valoracionService = new ValoracionService();
        this.anuncioService = new AnuncioService();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long anuncioId = Long.parseLong(request.getParameter("anuncioId"));

        // Obtener el anuncio y sus valoraciones
        Anuncio anuncio = anuncioService.findById(anuncioId);
        List<Valoracion> valoraciones = valoracionService.findValoracionesByAnuncio(anuncioId);

        // Obtener el usuario de la sesión
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Verificar si el usuario ha valorado el anuncio
        boolean haValorado = false;
        if (usuario != null) {
            haValorado = valoracionService.existeValoracionDeUsuarioParaAnuncio(usuario.getIdUsuario(), anuncioId);
        }

        // Pasar los atributos al JSP
        request.setAttribute("anuncio", anuncio);
        request.setAttribute("valoraciones", valoraciones);
        request.setAttribute("haValorado", haValorado); // Pasar el estado de valoración

        // Redirigir al JSP
        request.getRequestDispatcher("/WEB-INF/views/verValoraciones.jsp").forward(request, response);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}