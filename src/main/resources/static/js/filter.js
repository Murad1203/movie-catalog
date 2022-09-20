const filterName = document.getElementById("nameMovie"),
    filterGenre = document.getElementById("genreIsMovie"),
    filterLastname = document.getElementById("lastnameIsActor");

const button = document.getElementById("btn");





let nameValue, genreValue, lastnameValue;

button.addEventListener('click', () => {
    nameValue = filterName.value;
    genreValue = filterGenre.value;
    lastnameValue = filterLastname.value;

    console.log(nameValue + " " + genreValue + " " + lastnameValue);

    if (nameValue == "") {
        nameValue = "null"
    }
    if (genreValue == "") {
        genreValue = "null";
    }

    if (lastnameValue == "") {
        lastnameValue = "null";
    }

    document.location.href = "/filter/" + nameValue + "/" + genreValue + "/" + lastnameValue;
    nameValue.value = "";
    genreValue.value = "";
    lastnameValue.value = "";
})



