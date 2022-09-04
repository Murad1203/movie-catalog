const addInput = document.querySelector(".add-btn__item"),
    inputsContainer = document.querySelector(".inputs");

addInput.addEventListener("click", (e) => {
    e.preventDefault();

    const newInput = document.createElement("input");
    newInput.classList.add("form__input");
    newInput.setAttribute("type", "text");
    newInput.setAttribute("placeholder", "Введите ФИО актера");
    newInput.setAttribute('id', 'actors')
    newInput.setAttribute('name', 'actors')
    newInput.setAttribute('value', '')


    inputsContainer.appendChild(newInput)
})