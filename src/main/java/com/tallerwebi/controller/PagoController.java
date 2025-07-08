package com.tallerwebi.controller;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.model.Moneda;
import com.tallerwebi.model.Usuario;
import com.tallerwebi.service.IServicioUsuario;
import com.tallerwebi.service.ServicioTienda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Collections;
import io.github.cdimascio.dotenv.Dotenv;

@Controller
@RequestMapping("/pago")
public class PagoController {

    private final ServicioTienda servicioTienda;
    private final IServicioUsuario servicioUsuario;
    private final String publicUrl;

    @Autowired
    public PagoController(ServicioTienda servicioTienda, IServicioUsuario servicioUsuario, Dotenv dotenv) {
        this.servicioUsuario = servicioUsuario;
        this.servicioTienda = servicioTienda;
        this.publicUrl = dotenv.get("PUBLIC_URL");
    }

    @GetMapping("/moneda/{id}")
    public RedirectView pagarMoneda(@PathVariable Long id, HttpSession session, HttpServletRequest request) throws Exception {
        try {

            Moneda moneda = servicioTienda.obtenerMonedaPorId(id);

            session.setAttribute("MONEDA_COMPRADA", id);


            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title(moneda.getNombre())
                    .description("Compra de moneda en TP Taller Web")
                    .categoryId("others")
                    .quantity(1)
                    .unitPrice(BigDecimal.valueOf(moneda.getValor()))
                    .currencyId("ARS")
                    .build();

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(publicUrl + "/spring/pago/confirmacion")
                    .failure(publicUrl + "/spring/pago/fallo")
                    .pending(publicUrl + "/spring/pago/pendiente")
                    .build();

            PreferenceRequest requestMP = PreferenceRequest.builder()
                    .items(Collections.singletonList(item))
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .binaryMode(true)
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference response = client.create(requestMP);

            return new RedirectView(response.getInitPoint());
        } catch (com.mercadopago.exceptions.MPApiException e) {
            System.out.println("❌ ERROR DE MERCADO PAGO:");
            System.out.println(e.getApiResponse().getContent());
            throw e;
        }
    }

    @GetMapping("/confirmacion")
    public ModelAndView confirmarPago(@RequestParam(required = false) Long payment_id, @RequestParam(required = false) String status, HttpSession session) {

        ModelMap modelo = new ModelMap();

        Usuario usuario = (Usuario) session.getAttribute("USUARIO");

        if (usuario == null) {
            modelo.put("mensaje", "No se pudo verificar el usuario.");
            return new ModelAndView("confirmacion", modelo);
        }

        if (payment_id != null && "approved".equalsIgnoreCase(status)) {
            modelo.put("mensaje", "¡Compra realizada con éxito! ID de pago: " + payment_id);

            Long idMonedaComprada = (Long) session.getAttribute("MONEDA_COMPRADA");

            if (idMonedaComprada != null) {
                Moneda moneda = servicioTienda.obtenerMonedaPorId(idMonedaComprada);

                servicioUsuario.agregarMonedas(usuario.getId(), moneda.getCantidad());

                modelo.put("detalle", "Se agregaron " + moneda.getCantidad() + " monedas a tu cuenta.");

                session.removeAttribute("MONEDA_COMPRADA");
            } else {
                modelo.put("detalle", "No se pudo identificar qué moneda compraste.");
            }

        } else {
            modelo.put("mensaje", "El pago no fue aprobado.");
        }

        return new ModelAndView("confirmacion", modelo);
    }

    @GetMapping("/fallo")
    public ModelAndView pagoFallido() {
        ModelMap modelo = new ModelMap();
        modelo.put("mensaje", "El pago fue cancelado o falló.");
        return new ModelAndView("confirmacion", modelo);
    }

    @GetMapping("/pendiente")
    public ModelAndView pagoPendiente() {
        ModelMap modelo = new ModelMap();
        modelo.put("mensaje", "Tu pago está pendiente de aprobación.");
        return new ModelAndView("confirmacion", modelo);
    }

}
