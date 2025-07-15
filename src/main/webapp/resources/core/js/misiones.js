const URL = "/spring/api/misiones"
const containerMisiones = document.getElementById('containerMisiones')
import 'https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.20.1/cdn/components/progress-bar/progress-bar.js';

const header = document.querySelector('#header');
var notyf = new Notyf();

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
                header.classList.remove('hidden');
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

    return `
            <div class="container-trofeos">
                <div class="trofeos-content" >
                    <img alt="imagen de trofeo" src="/spring/imgs/LegendTrophy.webp"/>
                     <span>x${mision.copas}</span>           
                </div>
            </div>
            
            <div class="mision-content">
                <div class = 'container-img'>
                    <img data-bs-target="#exampleModal"
                        data-id ="${mision.id}"
                        class="cambiar-mision"
                        data-bs-toggle="modal"
                        src="/spring/imgs/arrow-counterclockwise.svg"
                        alt="">
                </div>
                
                
                <div class="mision-description">
                    <h2 class="mision-title">${mision.descripcion}</h2>
                    
                    <sl-progress-bar class="progreso" value="${porcentaje}">
                        <span class="progreso-text">${mision.progreso} / ${mision.cantidad}</span>
                    </sl-progress-bar>
                    
                </div>
                
            </div>
`;
}

const asignarIdMision = () => {
    const button = document.querySelectorAll('.cambiar-mision');

    button.forEach((e) => {
        e.addEventListener('click', (e) => {
            const id = e.target.getAttribute('data-id');
            document.querySelector('#confirmar-cambio').setAttribute('data-id', id);
        })
    })
}

const renderizarMisiones = (misiones) => {
    containerMisiones.innerHTML = '';

    if (misiones && misiones.length > 0) {
        misiones.forEach(mision => {
            const div = document.createElement('div');
            div.classList.add('card-misiones');
            div.innerHTML = generarContenidoHTML(mision);
            containerMisiones.appendChild(div);

            asignarIdMision();
        })
    } else {
        mostrarMensaje("No se encontraron misiones para el usuario");
    }
}

const mostrarLoader = () => {
    containerMisiones.innerHTML = '';
    const p = document.createElement('p');
    p.innerHTML = `<sl-spinner style="font-size: 3rem; --indicator-color:darkgreen ; --track-color: green;"></sl-spinner>`;
    containerMisiones.appendChild(p);
}

const cambiarMisiones = async () => {
    document.getElementById('confirmar-cambio').addEventListener('click', async (e) => {

        const id = e.target.getAttribute('data-id');
        mostrarLoader();
        cerrarModal();
        header.classList.add('hidden');


        try {
            const response = await fetch(`/spring/api/misiones/${id}`, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'}
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => null);
                throw new Error(errorData?.error || 'Hubo un problema al cambiar la mision');
            }

            const misiones = await obtenerFormatoJSON(response);
            renderizarMisiones(misiones);
            notyf.success('La mision se cambio con exito');

        } catch (error) {
            notyf.error('Hubo un problema al cambiar la mision');
            await obtenerMisiones();
        }
    });
}

const cerrarModal = () => {
    const modalElement = document.getElementById('exampleModal');
    const modalBootstrap = bootstrap.Modal.getInstance(modalElement);
    modalBootstrap.hide();
}

document.addEventListener('DOMContentLoaded', async () => {
    await obtenerMisiones();
    cambiarMisiones();
});




