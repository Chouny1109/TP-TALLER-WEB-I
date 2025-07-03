package com.tallerwebi.controller;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.model.Pregunta;
import com.tallerwebi.repository.impl.RepositorioPreguntaImpl;
import com.tallerwebi.service.ServicioEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/editor")
public class EditorController {

    private final ServicioEditor servicioEditor;

    @Autowired
    public EditorController(ServicioEditor servicioEditor) {
        this.servicioEditor = servicioEditor;
    }

    @GetMapping
    public ModelAndView vistaEditor(@RequestParam(value = "categoria", required = false) String categoria) {
        List<Pregunta> preguntas;

        if (categoria != null && !categoria.isEmpty()) {
            preguntas = servicioEditor.obtenerPreguntasPorCategoria(categoria);
        } else {
            preguntas = servicioEditor.ObtenerTodasLasPreguntas();
        }

        List<String> categorias = servicioEditor.obtenerCategorias(); // Para el select

        ModelMap model = new ModelMap();
        model.put("preguntas", preguntas);
        model.put("categorias", categorias);
        model.put("categoriaSeleccionada", categoria);

        return new ModelAndView("preguntasEditor", model);
    }

    @PostMapping("/editar")
    public String editarPregunta(HttpServletRequest request) {
        Long idPregunta = Long.valueOf(request.getParameter("idPregunta"));
        String enunciado = request.getParameter("enunciado");
        Long idRespuestaCorrecta = Long.valueOf(request.getParameter("respuestaCorrecta"));

        String[] ids = request.getParameterValues("respuestas[#idx].id");
        String[] textos = request.getParameterValues("respuestas[#idx].descripcion");

        servicioEditor.editarPreguntaYRespuestas(idPregunta, enunciado, ids, textos, idRespuestaCorrecta);

        return "redirect:/editor";
    }

    @PostMapping("/cambiarEstadoPregunta")
    public ModelAndView cambiarEstadoPregunta(@RequestParam("idPregunta")Long idPregunta){

        servicioEditor.cambiarEstadoPregunta(idPregunta);

        return new ModelAndView("redirect:/editor");
    }

    @PostMapping("/agregarPregunta")
    public String agregarPregunta(HttpServletRequest request) {
        String categoria = request.getParameter("categoria");
        String enunciado = request.getParameter("enunciado");
        Long idRespuestaCorrecta = Long.valueOf(request.getParameter("respuestaCorrecta"));
        String[] textos = request.getParameterValues("textos");

        servicioEditor.agregarPreguntasYRespuestas(categoria, enunciado, textos, idRespuestaCorrecta);

        return "redirect:/editor";
    }

    @PostMapping("/eliminarPregunta")
    public ModelAndView eliminarPregunta(@RequestParam("idPregunta")Long idPregunta){

        servicioEditor.eliminarPregunta(idPregunta);

        return new ModelAndView("redirect:/editor");
    }
}
