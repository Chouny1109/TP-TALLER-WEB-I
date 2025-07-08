const URL = "/spring/api/misiones"
const containerMisiones = document.getElementById('containerMisiones')
import 'https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.20.1/cdn/components/progress-bar/progress-bar.js';

async function obtenerFormatoJSON(response) {
    return await response.json();
}

const mostrarMensaje = (mensaje) => {
    containerMisiones.innerHTML = '';

    const p = document.createElement('p');
    p.innerText = mensaje;
    p.classList.add('mostrar-mensaje');
    containerMisiones.appendChild(p);
}

const obtenerMisiones = async () => {

    try {
        const response = await fetch(URL);
        const status = response.status;

        switch (status) {
            case 200: {
                const misiones = await obtenerFormatoJSON(response);
                renderizarMisiones(misiones);
                break;
            }

            case 204: {
                mostrarMensaje("No se encontraron misiones para el usuario");
                break;
            }

            case 401: {
                window.location.href = "/spring/login"
                break;
            }

            default: {
                const json = await obtenerFormatoJSON(response);
                mostrarMensaje(json.error);
                break;
            }
        }
    } catch (error) {
        mostrarMensaje("No se pudo conectar al servidor", error.message);
    }
}

const generarContenidoHTML = (mision) => {
    const porcentaje = Math.floor((mision.progreso * 100) / mision.cantidad);

    return `<div class="container-trofeos">
                <div class="trofeos-content" >
                    <img alt="imagen de trofeo" src="/spring/imgs/LegendTrophy.webp"/>
                     <span>x${mision.copas}</span>           
                </div>
            </div>
            <div class="mision-content">
                <img class="cambiar-mision" src="/spring/imgs/arrow-counterclockwise.svg" alt="">
                
                
                <div class="mision-description">
                    <h2 class="mision-title">${mision.descripcion}</h2>
                    
                    <sl-progress-bar class="progreso" value="${porcentaje}">
                        <span class="progreso-text">${mision.progreso} / ${mision.cantidad}</span>
                    </sl-progress-bar>
                    
                </div>
            </div>
`;
}

const renderizarMisiones = (misiones) => {
    containerMisiones.innerHTML = '';

    misiones.forEach(mision => {
        const div = document.createElement('div');
        div.classList.add('card-misiones');
        div.innerHTML = generarContenidoHTML(mision);

        containerMisiones.appendChild(div);
    })
}


document.addEventListener('DOMContentLoaded', obtenerMisiones);
