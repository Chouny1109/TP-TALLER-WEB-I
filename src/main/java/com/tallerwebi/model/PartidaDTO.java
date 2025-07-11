package com.tallerwebi.model;

    import java.util.List;
import java.util.stream.Collectors;

    public class PartidaDTO {

        private Long id;
        private String estado;
        private String tipo;
        private List<JugadorDTO> jugadores;

        private Long idTurnoActual;

        public void setIdTurnoActual(Long idTurnoActual) {
            this.idTurnoActual = idTurnoActual;
        }

        public Long getIdTurnoActual() {
            return idTurnoActual;
        }

        public PartidaDTO(Partida partida, List<Usuario> jugadores) {
            this.id = partida.getId();
            this.estado = partida.getEstadoPartida().toString(); // ejemplo: "EN_ESPERA", "EN_JUEGO", etc.
            this.tipo = partida.getTipo().toString();            // ejemplo: "CLASICO", "RANDOM", etc.

            this.jugadores = jugadores.stream()
                    .map(JugadorDTO::new)
                    .collect(Collectors.toList());
        }
        public PartidaDTO(Partida partida) {
            this.id = partida.getId();
            this.estado = partida.getEstadoPartida().toString();
            this.tipo = partida.getTipo().toString();
        }


        public PartidaDTO(Partida partida, List<Usuario> jugadores, Long usuarioId) {
            this(partida, jugadores); // llama al constructor original

            // Lógica para identificar al rival en Java y agregarlo como el último jugador del DTO
            if (jugadores != null && jugadores.size() == 2) {
                Usuario jugador1 = jugadores.get(0);
                Usuario jugador2 = jugadores.get(1);
                Usuario rival = jugador1.getId().equals(usuarioId) ? jugador2 : jugador1;

                // Lo agregamos como un jugador adicional o lo seteamos de alguna forma
                // Por ejemplo, si no querés agregar campos, podés dejarlo en la lista como está
                // O crear un método auxiliar `getRival(Long usuarioId)` si preferís no agregar nada
            }
        }

        public JugadorDTO getRival(Long usuarioId) {
            if (jugadores == null || jugadores.size() != 2) {
                // Retornar un JugadorDTO "vacío" con valores por defecto para evitar NPE en Thymeleaf
                JugadorDTO jugadorVacio = new JugadorDTO();
                jugadorVacio.setId(0L);
                jugadorVacio.setNombreUsuario("Rival no disponible");

                jugadorVacio.setLinkAvatar("/resources/imgs/default-avatar.png"); // o la ruta a tu avatar default
                return jugadorVacio;
            }

            JugadorDTO jugador1 = jugadores.get(0);
            JugadorDTO jugador2 = jugadores.get(1);

            return jugador1.getId().equals(usuarioId) ? jugador2 : jugador1;
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


