package com.tallerwebi.model;

    public class EstadoPartidaDTO {

        private boolean avanzarAutomaticamente;
        private String mensaje;
        private String estado; // "respondio", "esperandoRival", etc.

        // Getters y setters

        public boolean isAvanzarAutomaticamente() {
            return avanzarAutomaticamente;
        }

        public void setAvanzarAutomaticamente(boolean avanzarAutomaticamente) {
            this.avanzarAutomaticamente = avanzarAutomaticamente;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

    }

