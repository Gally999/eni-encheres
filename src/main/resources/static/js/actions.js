window.onload = focus();

let achatSelect = document.querySelector('select[name="achats"]');
let venteSelect = document.querySelector('select[name="ventes"]');

function disableSelected(number) {
    switch (number) {
        case 0 :
            achatSelect.setAttribute("disabled", "");
            venteSelect.removeAttribute("disabled");
            document.querySelector(".filter-form").submit();
            break;
        case 1 :
            venteSelect.setAttribute("disabled", "");
            achatSelect.removeAttribute("disabled");
            document.querySelector(".filter-form").submit();
            break;
    }
}

function submit(e) {
    document.querySelector(".filter-form").submit();
}

function focus() {
    let input = document.querySelector("input[name='keyword']");
    input.focus();
    let text = input.value;
    input.value = '';
    input.value = text;
}