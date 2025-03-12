let achatSelect = document.querySelector('select[name="achats"]');
let venteSelect = document.querySelector('select[name="ventes"]');

function disableSelected(number) {
    console.log("coucou je click " + number);
    console.log(achatSelect);
    console.log(venteSelect);
    switch (number) {
        case 0 :
           achatSelect.setAttribute("disabled", "");
           venteSelect.removeAttribute("disabled")
            break;
        case 1 :
            venteSelect.setAttribute("disabled", "");
            achatSelect.removeAttribute("disabled")
            break;
    }
}