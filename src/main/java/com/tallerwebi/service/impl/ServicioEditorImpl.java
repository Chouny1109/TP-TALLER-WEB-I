package com.tallerwebi.service.impl;

import com.tallerwebi.dominio.enums.CATEGORIA_PREGUNTA;
import com.tallerwebi.model.Pregunta;
import com.tallerwebi.model.Respuesta;
import com.tallerwebi.repository.RepositorioEditor;
import com.tallerwebi.repository.RepositorioPregunta;
import com.tallerwebi.service.ServicioEditor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicioEditorImpl implements ServicioEditor{

    private final RepositorioEditor repositorioEditor;
    private final RepositorioPregunta repositorioPregunta;

    public ServicioEditorImpl(RepositorioEditor repositorioEditor, RepositorioPregunta repositorioPregunta) {
        this.repositorioEditor = repositorioEditor;
        this.repositorioPregunta = repositorioPregunta;
    }


    @Override
    public List<Pregunta> ObtenerTodasLasPreguntas() {
        return repositorioEditor.ObtenerTodasLasPreguntas();
    }

    @Override
    public List<Pregunta> ObtenerPreguntasPorCategoria(CATEGORIA_PREGUNTA categoria) {
        return List.of();
    }

    @Override
    @Transactional
    public void editarPreguntaYRespuestas(Long idPregunta, String enunciado, String[] ids, String[] textos, Long idCorrecta) {
        Pregunta pregunta = repositorioPregunta.buscarPreguntaPorId(idPregunta);
        if (pregunta == null) return;

        pregunta.setEnunciado(enunciado);
        repositorioPregunta.actualizar(pregunta);

        for (int i = 0; i < ids.length; i++) {
            Long id = Long.valueOf(ids[i]);
            String texto = textos[i];

            Respuesta r = repositorioPregunta.buscarRespuestaPorId(id);
            if (r != null) {
                r.setDescripcion(texto);
                r.setOpcionCorrecta(r.getId().equals(idCorrecta));
                repositorioPregunta.actualizarRespuestas(r);
            }
        }
    }

    @Override
    @Transactional
    public void cambiarEstadoPregunta(Long id) {
        Pregunta pregunta = repositorioPregunta.buscarPreguntaPorId(id);
        if (pregunta != null) {
            pregunta.setHabilitada(!pregunta.getHabilitada());
            repositorioPregunta.actualizar(pregunta);
        }
    }
    @Override
    public List<String> obtenerCategorias() {
        return Arrays.stream(CATEGORIA_PREGUNTA.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pregunta> obtenerPreguntasPorCategoria(String categoria) {
        return repositorioPregunta.obtenerPreguntasPorCategoria(categoria);
    }

    @Override
    @Transactional
    public void agregarPreguntasYRespuestas(String categoria, String enunciado, String[] textos, Long idCorrecta) {
        Pregunta nuevaPregunta = new Pregunta();
        nuevaPregunta.setCategoria(CATEGORIA_PREGUNTA.valueOf(categoria));
        nuevaPregunta.setEnunciado(enunciado);

        List<Respuesta> respuestas = new ArrayList<>();

        for (int i = 0; i < textos.length; i++) {
            Respuesta respuesta = new Respuesta();
            respuesta.setDescripcion(textos[i]);
            respuesta.setOpcionCorrecta((long) i == idCorrecta);
            respuesta.setPregunta(nuevaPregunta);
            respuestas.add(respuesta);
        }

        nuevaPregunta.setRespuestas(respuestas);
        repositorioPregunta.agregarNuevaPregunta(nuevaPregunta);
    }

    @Override
    public void eliminarPregunta(Long idPregunta) {
        repositorioPregunta.eliminarPregunta(idPregunta);
    }
}
