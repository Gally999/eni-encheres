package fr.eni.ecole.projet.encheres.controller;



import java.security.Principal;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.eni.ecole.projet.encheres.bll.UtilisateurService;
import fr.eni.ecole.projet.encheres.bo.Adresse;
import fr.eni.ecole.projet.encheres.bo.Utilisateur;
import fr.eni.ecole.projet.encheres.exceptions.BusinessException;
import jakarta.validation.Valid;

@Controller
@Validated
public class UtilisateurController {
	
	
	private UtilisateurService utilisateurService;
	
	
	public UtilisateurController (UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}
		
	
	// MODIFIER MOT DE PASSE
	@GetMapping("/modifier-motDePasse")
	public String modifierMotDePasse(Principal principal, Model model) {
	    String pseudo = principal.getName();  
	    
	    
	    System.out.println("Utilisateur avant mise à jour Modif MDP: " + pseudo);

	    
	    if (pseudo != null) {
	        model.addAttribute("pseudo", pseudo); 
	        return "view-modifier-motDePasse"; 
	    } else {
	        System.out.println("Aucun utilisateur trouvé.");
	        return "view-encheres"; 
	    }
	}

		
		
	@PostMapping("/modifier-motDePasse")
	public String modifierMotDePasse(
	    @RequestParam("motDePasseSaisi") String motDePasseSaisi,
	    @RequestParam("motDePasseNew") String motDePasseNew,
	    @RequestParam("motDePasseConfirmation") String motDePasseConfirmation,
	    Principal principal) {

	    System.out.println("Méthode POST appelée - Modifier le mot de passe");
	    System.out.println("motDePasseSaisi" + motDePasseSaisi);
	    System.out.println("motDePasseNew" + motDePasseNew);
	    System.out.println("motDePasseConfirmation" + motDePasseConfirmation);

	    // Récupérer le pseudo de l'utilisateur connecté
	    String pseudo = principal.getName();
	    System.out.println("Pseudo de l'utilisateur connecté : " + pseudo);

	    Utilisateur utilisateur = utilisateurService.findByPseudo(pseudo);
	    
	    if (utilisateur == null) {
	        System.out.println("Utilisateur non trouvé");
	        return "view-modifier-motDePasse";  
	    }
	    System.out.println("Utilisateur trouvé : " + utilisateur.getPseudo());

	    // Vérification du mot de passe saisi
	    System.out.println("Vérification du mot de passe actuel");
	    if (motDePasseSaisi == null || motDePasseSaisi.trim().isEmpty() || !utilisateurService.verifierMotDePasse(motDePasseSaisi, utilisateur)) {
	        System.out.println("Mot de passe saisi incorrect");
	        return "view-modifier-motDePasse";  
	    } else {
	        System.out.println("Mot de passe saisi correct");
	    }

	    // Vérification du nouveau mot de passe et de la confirmation
	    System.out.println("Vérification du nouveau mot de passe et de la confirmation");
	    if (motDePasseNew == null || motDePasseNew.trim().isEmpty() || motDePasseConfirmation == null || motDePasseConfirmation.trim().isEmpty() || !motDePasseNew.equals(motDePasseConfirmation)) {
	        System.out.println("Les mots de passe ne correspondent pas ou sont vides");
	        return "view-modifier-motDePasse";  
	        
	    }

	    try {
	        // Appel au service pour mettre à jour le mot de passe
	        utilisateurService.mettreAjourMotDePasse(motDePasseNew, utilisateur);
	        System.out.println("Mot de passe mis à jour avec succès pour l'utilisateur : " + pseudo);
	    } catch (BusinessException e) {
	        System.out.println("Erreur lors de la mise à jour du mot de passe : " + e.getMessage());
	        return "view-modifier-motDePasse"; 
	    }

	    return "redirect:/monProfil/detail";  
	}

    
	// SUPPRIMER MON PROFIL
    @PostMapping("/supprimer-compte")
	public String supprimerCompte(Principal principal, RedirectAttributes redirectAttributes) {
	    System.out.println("Requête POST reçue pour supprimer le compte");

	    String pseudo = principal.getName();
	    System.out.println("Pseudo de l'utilisateur connecté : " + pseudo);

	    // Supprimer l'utilisateur de la base de données
	    boolean success = utilisateurService.supprimerUtilisateur(pseudo);

	    if (success) {
	        // Si la suppression a réussi
	        redirectAttributes.addFlashAttribute("message", "Votre compte a été supprimé avec succès.");
	        return "view-utilisateur-creer";
	    } else {
	        // En cas d'échec
	        redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du compte.");
	        return "redirect:/monProfil/detail";
	    }
	}

    //PAGE MODIFIER MON PROFIL
    @PostMapping("/monProfil/detail")
    public String modifierProfil(
    		@Valid 
    		@ModelAttribute("utilisateur") 
    		Utilisateur utilisateur,
    		BindingResult bindingResult,
    		Model model) {

    	System.out.println("Entrée dans la méthode modifierProfil");

    	// Vérification des erreurs de validation
    	if (bindingResult.hasErrors()) {
    		System.out.println("Il y a des erreurs de validation.");
    		model.addAttribute("utilisateur", utilisateur);
    		return "view-mon-profil-detail";
    	} else {
    		try {
    			// On ne modifie pas le pseudo, on le réaffecte à l'ancien pseudo
    			//	            String pseudoActuel = utilisateur.getPseudo();
    			//	            utilisateur.setPseudo(pseudoActuel);

    			// On ne modifie pas le credit, on le réaffecte à l'ancien pseudo
    			//	            int creditActuel = utilisateur.getCredit();
    			//	            utilisateur.setCredit(creditActuel);

    			System.out.println(utilisateur);

    			// Mise à jour de l'utilisateur
    			utilisateurService.mettreAjourUtilisateur(utilisateur);

    			System.out.println("Utilisateur et adresse mis à jour avec succès.");
    			return "view-mon-profil-detail";
    		} catch (BusinessException e) {
    			// Gestion des erreurs de business logic
    			e.getClefsExternalisations().forEach(key -> {
    				ObjectError error = new ObjectError("globalError", key);
    				bindingResult.addError(error);
    			});
    			return "view-mon-profil-detail";
    		}
    	}
    }


	 
  	@GetMapping("/monProfil/detail")
  	public String afficherProfil(Principal principal, Model model) {
  		String pseudo = principal.getName();  

  		Utilisateur utilisateur = utilisateurService.findByPseudo(pseudo);

  		Adresse adresse = utilisateurService.getAdresseDeLUtilisateurConnecte(pseudo);

  		// Vérifier si l'adresse est correctement récupérée
  		System.out.println("Adresse : " + adresse);
  		if (adresse != null) {
  			System.out.println("Adresse : " + adresse);
  			System.out.println("Rue: " + adresse.getRue());
  			System.out.println("Code Postal: " + adresse.getCodePostal());
  			System.out.println("Ville: " + adresse.getVille());
  			utilisateur.setAdresse(adresse);
  		} else {
  			System.out.println("Aucune adresse trouvée.");
  			adresse = new Adresse();      

  		}

  		// Passer l'objet adresse au modèle
  		model.addAttribute("utilisateur", utilisateur);
  		System.out.println("Adresse dans le modèle : " + model.getAttribute("adresse"));

  		return "view-mon-profil-detail"; 
  	}


	
	//PAGE MON PROFIL:
	@GetMapping("/monProfil")
	public String detailUtilisateur(Principal principal, Model model) {
		// Récupérer le pseudo de l'utilisateur connecté
		String pseudo = principal.getName();
		System.out.println("Pseudo de l'utilisateur connecté : " + pseudo);

		// Récupérer l'utilisateur à partir de son pseudo
		Utilisateur utilisateur = utilisateurService.findByPseudo(pseudo);
		if (utilisateur != null) {
			System.out.println("Utilisateur trouvé : " + utilisateur.getNom() + " " + utilisateur.getPrenom());
		} else {
			System.out.println("Utilisateur non trouvé !");
		}


		String telephone = utilisateurService.getTelephoneDeLUtilisateurConnecte(pseudo);
		System.out.println("Téléphone récupéré : " + telephone);


		model.addAttribute("utilisateur", utilisateur);
		model.addAttribute("telephone", telephone);

		return "view-mon-profil"; 
	}



	//PAGE S'INSCRIRE:		
	// Création d'un nouvel utilisateur
	@GetMapping("/creer")
	public String creerUtilisateur(Model model) {
		Utilisateur utilisateur = new Utilisateur();
		// Ajout de l'instance dans le modèle
		model.addAttribute("utilisateur", utilisateur);
		return "view-utilisateur-creer";
	}


	@PostMapping("/creer")
	public String creerUtilisateur(
			@RequestParam("motDePasse") String motDePasse,  
			@RequestParam("motDePasseConfirmation") String motDePasseConfirmation,  
			@ModelAttribute("utilisateur") Utilisateur utilisateur,  
			BindingResult bindingResult, 
			Model model) {

		// Affichage des valeurs reçues dans le formulaire
		System.out.println("Mot de passe saisi : " + motDePasse);
		System.out.println("Mot de passe de confirmation : " + motDePasseConfirmation);

		// Vérification que les mots de passe correspondent
		if (motDePasse == null || !motDePasse.equals(motDePasseConfirmation)) {
			System.out.println("Erreur : les mots de passe ne correspondent pas.");
			bindingResult.rejectValue("motDePasseConfirmation", "validation.utilisateur.motDePasse.confirmation");
		} else {
			System.out.println("Les mots de passe correspondent.");
		}

		// Si des erreurs de validation existent
		if (bindingResult.hasErrors()) {
			System.out.println("Des erreurs de validation ont été détectées.");
			bindingResult.getAllErrors().forEach(error -> {
				System.out.println("Erreur : " + error.getDefaultMessage());
			});
			return "view-utilisateur-creer"; // Retourner à la page de création de l'utilisateur
		} else {
			System.out.println("Aucune erreur de validation. Création de l'utilisateur.");

			// Si aucun problème, on affecte le mot de passe à l'utilisateur
			utilisateur.setMotDePasse(motDePasse);  
			utilisateur.setCredit(10);  // Crédit par défaut

			try {
				System.out.println("Tentative d'ajouter l'utilisateur dans la base de données.");
				utilisateurService.add(utilisateur);  // Sauvegarde dans la base de données via le service

				//Ajout du message de succès uniquement en cas de création réussie
				model.addAttribute("popupMessage", "Votre compte est créé avec succès, félicitations !");
				model.addAttribute("popupType", "success"); 

				System.out.println("Utilisateur ajouté avec succès.");
				return "redirect:/";
				// Rediriger vers la page d'accueil après la création
			} catch (BusinessException e) {
				System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
				// Ajouter des erreurs globales si une exception se produit
				e.getClefsExternalisations().forEach(key -> {
					ObjectError error = new ObjectError("globalError", key);
					bindingResult.addError(error);
					System.out.println("Erreur globale : " + key);
				});
				return "view-utilisateur-creer";  
			}
		}
	}


}




