package com.tallerwebi.model;

    public class EstadoPartidaDTO {

        private boolean avanzarAutomaticamente;
        private String mensaje;
        private String estado; // "respondio", "esperandoRival", etc.
        private Long idPartida;
        private String nombreRival;
        private String avatarUrlRival;
        private Long usuarioId;

        public Long getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
        }


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

        public Long getIdPartida() {
            return idPartida;
        }

        public void setIdPartida(Long idPartida) {
            this.idPartida = idPartida;
        }

        public String getNombreRival() {
            return nombreRival;
        }

        public void setNombreRival(String nombreRival) {
            this.nombreRival = nombreRival;
        }

        public String getAvatarUrlRival() {
            return avatarUrlRival;
        }

        public void setAvatarUrlRival(String avatarUrlRival) {
            this.avatarUrlRival = avatarUrlRival;
        }
    }

