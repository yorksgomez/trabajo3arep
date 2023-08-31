const CLIENT_URL = "http://localhost:35000/";
        
let form = document.querySelector("#search");
form.addEventListener('submit', (ev) => {
    ev.preventDefault();
    let url = CLIENT_URL + "api?q=" + form.elements.movie.value;

    fetch (url, {method: 'GET'})
        .then(res => res.json())
        .then(json => {
            let html = `
                <b>Titulo:</b> ${json.Title}<br>
                <b>Año de salida:</b> ${json.Released}<br>
                <b>Género:</b> ${json.Genre}<br>
                <b>Sinopsis:</b> ${json.Plot}<br>
            `;
            document.querySelector("#result").innerHTML = html;
        });
});