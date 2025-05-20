const buttonsForm = document.querySelectorAll('#form-setting button[type="button"]');
buttonsForm.forEach((button) => {
    const input = document.getElementById(button.getAttribute('data-target'));
    const inputValue = input.value;
    const buttonSubmit = document.querySelector('#form-setting button[type="submit"]');

    input.addEventListener('keyup', () => {
        //pregunta si el input esta vacio o si el contenido es igual al original --> en ese caso no deja enviar el form
        input.value===''||input.value===inputValue?buttonSubmit.disabled = true
            :buttonSubmit.disabled = false;
    })

    button.addEventListener('click', (event) => {
        input.readOnly = false;
        input.focus();
        desactivarOtros(input);

    })
    function desactivarOtros(){
        const inputs = document.querySelectorAll('#form-setting input')
        const otherInputs = Array.from(inputs).filter(e => e !== input);
        const inputEmpty = Array.from(inputs).find(e => e.value === "");

        otherInputs.forEach((e)=>{
            if(e.value === '' && inputEmpty){
                e.value=inputEmpty.getAttribute('data-original');
            }
            e.readOnly = true;
        })
    }
})