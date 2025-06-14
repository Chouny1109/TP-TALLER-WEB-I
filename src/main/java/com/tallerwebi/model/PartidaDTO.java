package com.tallerwebi.model;

    import java.util.List;
import java.util.stream.Collectors;

    public class PartidaDTO {

        private Long id;
        private String estado;
        private String tipo;
        private List<JugadorDTO> jugadores;

        public PartidaDTO(Partida partida, List<Usuario> jugadores) {
            this.id = partida.getId();
            this.estado = partida.getEstadoPartida().toString(); // ejemplo: "EN_ESPERA", "EN_JUEGO", etc.
            this.tipo = partida.getTipo().toString();            // ejemplo: "CLASICO", "RANDOM", etc.

            this.jugadores = jugadores.stream()
                    .map(JugadorDTO::new)
                    .collect(Collectors.toList());
        }

        // Getters y setters
        public Long getId() {
            return id;
        }

        public String getEstado() {
            return estado;
        }

        public String getTipo() {
            return tipo;
        }

        public List<JugadorDTO> getJugadores() {
            return jugadores;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public void setJugadores(List<JugadorDTO> jugadores) {
            this.jugadores = jugadores;
        }
    }


