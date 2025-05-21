const buttonsForm = document.querySelectorAll('#form-setting button[type="button"]');
const buttonSubmit = document.querySelector('#form-setting button[type="submit"]');

buttonsForm.forEach((button) => {
    const input = document.getElementById(button.getAttribute('data-target'));

    input.addEventListener('keyup', () => {
      const originalValue = input.getAttribute('data-original')
        buttonSubmit.disabled = input.value ===''||input.value ===originalValue;
    })

    button.addEventListener('click', (event) => {
        input.readOnly = false;
        input.focus();
        desactivarOtros(input);
    })

    function desactivarOtros(){
        const inputs = document.querySelectorAll('#form-setting input')
        const otherInputs = Array.from(inputs).filter(e => e !== input);

        otherInputs.forEach((e)=>{
            if(e.value === ''){
                const original = e.getAttribute('data-original')
                if(original!==null){
                    e.value=original;
                }
            }
            e.readOnly = true;
        })
    }
})