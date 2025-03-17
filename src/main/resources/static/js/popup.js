document.addEventListener('DOMContentLoaded', function () {
	 
	 console.log("scripts.js est chargé et exécuté");
	 
    // Récupérer l'élément de popup
    const popupContainer = document.getElementById('popup-container');

    // Vérifier s'il existe des messages flash dans le DOM
    const successMessage = document.getElementById('message');
    const errorMessage = document.getElementById('error');

    // Afficher la popup de succès si un message est présent
    if (successMessage) {
        showPopup(successMessage.innerText, 'success');
    }

    // Afficher la popup d'erreur si un message d'erreur est présent
    if (errorMessage) {
        showPopup(errorMessage.innerText, 'error');
    }

    // Fonction pour afficher une popup
    function showPopup(message, type) {
        // Créer l'élément de la popup
        const popup = document.createElement('div');
        popup.classList.add('popup', type);
        popup.innerText = message;

        // Ajouter la popup au conteneur
        popupContainer.appendChild(popup);

        // Fermer la popup après 5 secondes
        setTimeout(function () {
            popup.remove();
        }, 5000);
    }
    
    console.log("Success message:", successMessage);
	console.log("Error message:", errorMessage);
    
});
