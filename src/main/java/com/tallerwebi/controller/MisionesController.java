package com.tallerwebi.controller;

import com.tallerwebi.dominio.excepcion.UsuarioNoExistente;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.model.UsuarioMisionDTO;
import com.tallerwebi.service.ServicioMisionesUsuario;
import com.tallerwebi.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/misiones")
public class MisionesController {

//    private final ServicioMisionesUsuario servicioMisionesUsuario;
//    private final SessionUtil sessionUtil;
//
//    @Autowired
//    public MisionesController(ServicioMisionesUsuario servicioMisionesUsuario, SessionUtil sessionUtil) {
//        this.servicioMisionesUsuario = servicioMisionesUsuario;
//        this.sessionUtil = sessionUtil;
//    }

    @GetMapping
    //public ModelAndView obtenerMisiones(HttpServletRequest request)throws UsuarioNoExistente
    public String cargarVistaMisiones() {
//        ModelMap modelMap = new ModelMap();
//
//        Usuario logueado = this.sessionUtil.getUsuarioLogueado(request);
//
//        List<UsuarioMisionDTO> misionesDelUsuario = this.servicioMisionesUsuario.
//                obtenerLasMisionesDelUsuarioPorId(logueado.getId(), LocalDate.now());
//
//        modelMap.addAttribute("misiones", misionesDelUsuario);
//
//        return new ModelAndView("misiones", modelMap);
        return "misiones";
    }

//    @PostMapping("/cambiarMision")
//    public String cambiarMision(@RequestParam Long idMision, HttpServletRequest request) {
//        Usuario logueado = sessionUtil.getUsuarioLogueado(request);
//        servicioMisionesUsuario.cambiarMision(logueado, idMision);
//        return "redirect:/misiones";
//    }

}
