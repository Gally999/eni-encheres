
// Fonction de validation du mot de passe
function validerMotDePasse() {
   
    // Récupérer les valeurs des champs mot de passe et confirmation
    var mdp = document.getElementById("mdp").value;
    var mdpConfirmation = document.getElementById("motDePasseConfirmation").value;
    
    // Récupérer l'élément d'erreur
    var errorElement = document.getElementById("error-motDePasse");
    
    // Vérifier si les mots de passe correspondent
    if (mdp !== mdpConfirmation) {
        // Si les mots de passe ne correspondent pas, afficher un message d'erreur
        errorElement.style.display = "block";
        errorElement.innerHTML = "Les mots de passe ne correspondent pas.";
        return false; // Ne pas soumettre le formulaire
    } else {
        // Si les mots de passe correspondent, masquer le message d'erreur
        errorElement.style.display = "none";
        return true; // Soumettre le formulaire
    }
}


// Ajouter un écouteur d'événement pour la soumission du formulaire
document.addEventListener("DOMContentLoaded", function () {
    // Ajouter un gestionnaire d'événements au formulaire
    var form = document.querySelector("form");
    if (form) {
        form.onsubmit = function (event) {
            // Appeler la fonction de validation du mot de passe
            if (!validerMotDePasse()) {
                event.preventDefault(); // Annuler la soumission du formulaire
            }
        };
    }
});
